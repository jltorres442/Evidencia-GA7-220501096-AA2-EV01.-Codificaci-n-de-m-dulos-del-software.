package co.homecenter.envios.modelo;

import java.time.LocalDateTime;

/**
 * Representa un registro de la tabla {@code Cliente}: la persona que
 * realiza pedidos en Homecenter.
 */
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private Integer idCliente;
    private String direccion;

    /** Momento en que se creó la cuenta (lo asigna la base de datos). */
    private LocalDateTime fechaRegistro;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
