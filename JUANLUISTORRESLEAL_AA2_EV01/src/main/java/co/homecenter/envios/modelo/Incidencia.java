package co.homecenter.envios.modelo;

import java.io.Serializable;

/**
 * Representa un registro de la tabla {@code Incidencia}: un problema
 * reportado durante la entrega de un envío.
 */
public class Incidencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idIncidencia;
    private String descripcion;
    private String estado;
    private Integer idEnvio;

    /** Dirección del envío afectado (dato de consulta). */
    private String direccionEnvio;

    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
