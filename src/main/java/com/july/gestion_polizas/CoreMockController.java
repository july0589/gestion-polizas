
package com.july.gestion_polizas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    private static final Logger log =
            LoggerFactory.getLogger(CoreMockController.class);

    @PostMapping("/evento")
    public ResponseEntity<Map<String, String>> registrarEvento(
            @RequestBody Map<String, Object> solicitud) {

        Object evento = solicitud.get("evento");
        Object polizaId = solicitud.get("polizaId");

        if (evento == null || polizaId == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error",
                            "Los campos evento y polizaId son obligatorios"));
        }

        log.info("Intento de envío al CORE: evento={}, polizaId={}",
                evento, polizaId);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Evento registrado en el mock CORE",
                "evento", evento.toString(),
                "polizaId", polizaId.toString()
        ));
    }
}