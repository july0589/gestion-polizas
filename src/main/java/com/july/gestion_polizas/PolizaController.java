
package com.july.gestion_polizas;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/polizas")
public class PolizaController {

    private final PolizaService polizaService;

    public PolizaController(PolizaService polizaService) {
        this.polizaService = polizaService;
    }

    @GetMapping
    public List<Poliza> listarPolizas(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estado) {

        return polizaService.listarTodas().stream()
                .filter(p -> tipo == null
                        || p.getTipo().equalsIgnoreCase(tipo))
                .filter(p -> estado == null
                        || p.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPoliza(@PathVariable Long id) {
        return polizaService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/riesgos")
    public ResponseEntity<?> listarRiesgos(@PathVariable Long id) {
        if (polizaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> resultado =
                polizaService.listarRiesgosPorPoliza(id).stream()
                        .map(r -> Map.<String, Object>of(
                                "id", r.getId(),
                                "descripcion", r.getDescripcion(),
                                "estado", r.getEstado()))
                        .toList();

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/{id}/riesgos")
    public ResponseEntity<?> agregarRiesgo(
            @PathVariable Long id,
            @RequestBody Map<String, String> solicitud) {

        String descripcion = solicitud.get("descripcion");

        if (descripcion == null || descripcion.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "La descripción es obligatoria"));
        }

        try {
            Riesgo riesgo =
                    polizaService.agregarRiesgo(id, descripcion.trim());

            return ResponseEntity.status(201).body(Map.of(
                    "id", riesgo.getId(),
                    "descripcion", riesgo.getDescripcion(),
                    "estado", riesgo.getEstado(),
                    "polizaId", id));

        } catch (IllegalArgumentException e) {
            if ("La póliza no existe".equals(e.getMessage())) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<?> renovarPoliza(@PathVariable Long id) {
        return polizaService.buscarPorId(id).map(poliza -> {
            if ("CANCELADA".equals(poliza.getEstado())) {
                return ResponseEntity.badRequest().body(
                        Map.of("error",
                                "No se puede renovar una póliza cancelada"));
            }

            if ("RENOVADA".equals(poliza.getEstado())) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "La póliza ya está renovada"));
            }

            double nuevoCanon =
                    poliza.getCanonMensual() * (1 + poliza.getIpc());

            poliza.setCanonMensual(nuevoCanon);
            poliza.setPrima(nuevoCanon * poliza.getMesesVigencia());
            poliza.setEstado("RENOVADA");

            return ResponseEntity.ok(polizaService.guardar(poliza));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPoliza(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    polizaService.cancelarPoliza(id));
        } catch (IllegalArgumentException e) {
            if ("La póliza no existe".equals(e.getMessage())) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/riesgos/{riesgoId}/cancelar")
    public ResponseEntity<?> cancelarRiesgo(
            @PathVariable Long riesgoId) {
        try {
            Riesgo riesgo =
                    polizaService.cancelarRiesgo(riesgoId);

            return ResponseEntity.ok(Map.of(
                    "id", riesgo.getId(),
                    "descripcion", riesgo.getDescripcion(),
                    "estado", riesgo.getEstado()));

        } catch (IllegalArgumentException e) {
            if ("El riesgo no existe".equals(e.getMessage())) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage()));
        }
    }
}
