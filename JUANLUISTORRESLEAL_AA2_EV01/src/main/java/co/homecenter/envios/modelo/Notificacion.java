package co.homecenter.envios.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa un registro de la tabla {@code Notificacion}: un mensaje
 * enviado a un transportista.
 */
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idNotificacion;
    private String mensaje;
    private LocalDateTime fechaHora;
    private Integer idTransportista;

    /** Nombre del transportista destinatario (dato de consulta). */
    private String nombreTransportista;

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }
}
