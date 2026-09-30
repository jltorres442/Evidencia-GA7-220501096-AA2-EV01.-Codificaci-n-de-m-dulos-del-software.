package co.homecenter.envios.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa un registro de la tabla {@code Pedido}.
 *
 * <p>Además de las columnas propias, guarda el nombre del cliente y del
 * vendedor obtenidos con {@code JOIN}, para mostrarlos en los listados sin
 * hacer consultas adicionales.</p>
 */
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPedido;
    private LocalDate fecha;
    private BigDecimal total;
    private String estado;
    private Integer idCliente;
    private Integer idVendedor;

    /** Nombre del cliente (dato de consulta). */
    private String nombreCliente;

    /** Nombre del vendedor (dato de consulta). */
    private String nombreVendedor;

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
}
