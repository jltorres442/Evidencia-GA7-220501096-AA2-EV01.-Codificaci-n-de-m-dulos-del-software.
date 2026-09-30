package co.homecenter.envios.modelo;

/**
 * Representa un registro de la tabla {@code Vendedor}: el asesor comercial
 * que registra los pedidos de los clientes.
 */
public class Vendedor extends Usuario {

    private static final long serialVersionUID = 1L;

    private Integer idVendedor;

    /** Indica si el vendedor está habilitado para registrar pedidos. */
    private boolean registroPedido = true;

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public boolean isRegistroPedido() {
        return registroPedido;
    }

    public void setRegistroPedido(boolean registroPedido) {
        this.registroPedido = registroPedido;
    }
}
