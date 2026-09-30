package co.homecenter.envios.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa un registro de la tabla {@code Factura}. Cada pedido tiene
 * como máximo una factura (relación 1:1).
 */
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idFactura;
    private LocalDate fechaEmision;
    private BigDecimal montoTotal;
    private Integer idPedido;

    /** Nombre del cliente del pedido facturado (dato de consulta). */
    private String nombreCliente;

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
