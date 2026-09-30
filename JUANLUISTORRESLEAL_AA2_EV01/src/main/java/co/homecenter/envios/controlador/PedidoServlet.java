package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.ClienteDao;
import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.dao.PedidoDao;
import co.homecenter.envios.dao.VendedorDao;
import co.homecenter.envios.modelo.Pedido;
import co.homecenter.envios.util.CatalogoEstados;
import co.homecenter.envios.util.LectorFormulario;
import java.time.LocalDate;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Módulo CRUD de pedidos. Atiende la ruta {@code /pedidos}.
 */
@WebServlet(name = "PedidoServlet", urlPatterns = "/pedidos")
public class PedidoServlet extends CrudServlet<Pedido> {

    private static final long serialVersionUID = 1L;

    private final PedidoDao pedidoDao = new PedidoDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final VendedorDao vendedorDao = new VendedorDao();

    @Override
    protected CrudDao<Pedido> getDao() {
        return pedidoDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "pedidos";
    }

    @Override
    protected String getRutaModulo() {
        return "/pedidos";
    }

    @Override
    protected String getNombreEntidad() {
        return "El pedido";
    }

    /** Un pedido nuevo inicia con la fecha de hoy y en estado PENDIENTE. */
    @Override
    protected Pedido crearEntidadVacia() {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(CatalogoEstados.ESTADOS_PEDIDO.get(0));
        return pedido;
    }

    @Override
    protected Pedido leerFormulario(LectorFormulario formulario, Integer id) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(id);
        pedido.setFecha(formulario.fecha("fecha", "La fecha", true));
        pedido.setTotal(formulario.decimal("total", "El total", true));
        pedido.setEstado(formulario.opcion("estado", "El estado", CatalogoEstados.ESTADOS_PEDIDO));
        pedido.setIdCliente(formulario.entero("idCliente", "El cliente", true));
        pedido.setIdVendedor(formulario.entero("idVendedor", "El vendedor", true));
        return pedido;
    }

    /** Clientes y vendedores para las listas desplegables del formulario. */
    @Override
    protected void cargarCatalogos(HttpServletRequest solicitud) throws DaoException {
        solicitud.setAttribute("clientes", clienteDao.listar(null));
        solicitud.setAttribute("vendedores", vendedorDao.listar(null));
    }
}
