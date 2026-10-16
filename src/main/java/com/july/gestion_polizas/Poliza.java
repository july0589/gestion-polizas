
package com.july.gestion_polizas;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String estado;
    private Double canonMensual;
    private Integer mesesVigencia;
    private Double ipc;
    private Double prima;

    public Poliza() {
    }

    public Poliza(
            String tipo,
            String estado,
            Double canonMensual,
            Integer mesesVigencia,
            Double ipc,
            Double prima) {
        this.tipo = tipo;
        this.estado = estado;
        this.canonMensual = canonMensual;
        this.mesesVigencia = mesesVigencia;
        this.ipc = ipc;
        this.prima = prima;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCanonMensual() {
        return canonMensual;
    }

    public void setCanonMensual(Double canonMensual) {
        this.canonMensual = canonMensual;
    }

    public Integer getMesesVigencia() {
        return mesesVigencia;
    }

    public void setMesesVigencia(Integer mesesVigencia) {
        this.mesesVigencia = mesesVigencia;
    }

    public Double getIpc() {
        return ipc;
    }

    public void setIpc(Double ipc) {
        this.ipc = ipc;
    }

    public Double getPrima() {
        return prima;
    }

    public void setPrima(Double prima) {
        this.prima = prima;
    }
}