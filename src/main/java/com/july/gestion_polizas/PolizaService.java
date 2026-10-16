
package com.july.gestion_polizas;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;

    public PolizaService(
            PolizaRepository polizaRepository,
            RiesgoRepository riesgoRepository) {
        this.polizaRepository = polizaRepository;
        this.riesgoRepository = riesgoRepository;
    }

    public List<Poliza> listarTodas() {
        return polizaRepository.findAll();
    }

    public Optional<Poliza> buscarPorId(Long id) {
        return polizaRepository.findById(id);
    }

    public Poliza guardar(Poliza poliza) {
        return polizaRepository.save(poliza);
    }

    public List<Riesgo> listarRiesgosPorPoliza(Long polizaId) {
        return riesgoRepository.findByPolizaId(polizaId);
    }

    public Riesgo guardarRiesgo(Riesgo riesgo) {
        return riesgoRepository.save(riesgo);
    }

    @Transactional
    public Riesgo agregarRiesgo(
            Long polizaId, String descripcion) {

        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La póliza no existe"));

        if (!"COLECTIVA".equalsIgnoreCase(poliza.getTipo())) {
            throw new IllegalArgumentException(
                    "Solo se pueden agregar riesgos a pólizas colectivas");
        }

        if ("CANCELADA".equalsIgnoreCase(poliza.getEstado())) {
            throw new IllegalArgumentException(
                    "No se pueden agregar riesgos a una póliza cancelada");
        }

        Riesgo riesgo = new Riesgo(
                descripcion, "ACTIVO", poliza);

        return riesgoRepository.save(riesgo);
    }

    @Transactional
    public Riesgo cancelarRiesgo(Long riesgoId) {
        Riesgo riesgo = riesgoRepository.findById(riesgoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El riesgo no existe"));

        if ("CANCELADO".equalsIgnoreCase(riesgo.getEstado())) {
            throw new IllegalArgumentException(
                    "El riesgo ya está cancelado");
        }

        riesgo.setEstado("CANCELADO");

        return riesgoRepository.save(riesgo);
    }

    @Transactional
    public Poliza cancelarPoliza(Long polizaId) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La póliza no existe"));

        if ("CANCELADA".equalsIgnoreCase(poliza.getEstado())) {
            throw new IllegalArgumentException(
                    "La póliza ya está cancelada");
        }

        List<Riesgo> riesgos =
                riesgoRepository.findByPolizaId(polizaId);

        for (Riesgo riesgo : riesgos) {
            riesgo.setEstado("CANCELADO");
        }

        riesgoRepository.saveAll(riesgos);

        poliza.setEstado("CANCELADA");

        return polizaRepository.save(poliza);
    }
}
