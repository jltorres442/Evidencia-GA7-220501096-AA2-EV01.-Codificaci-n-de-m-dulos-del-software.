package co.homecenter.envios.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un registro de la tabla {@code Envio_Pedido}: el despacho
 * físico de un pedido, gestionado por el personal de logística.
 */
public class EnvioPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEnvio;
    private LocalDate fechaEnvio;
    private String direccionEntrega;
    private String estado;
    private Integer idPedido;
    private Integer idLogistica;

    /** Nombre del cliente del pedido enviado (dato de consulta). */
    private String nombreCliente;

    /** Nombre del responsable de logística (dato de consulta). */
    private String nombreLogistica;

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getIdLogistica() {
        return idLogistica;
    }

    public void setIdLogistica(Integer idLogistica) {
        this.idLogistica = idLogistica;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreLogistica() {
        return nombreLogistica;
    }

    public void setNombreLogistica(String nombreLogistica) {
        this.nombreLogistica = nombreLogistica;
    }
}
