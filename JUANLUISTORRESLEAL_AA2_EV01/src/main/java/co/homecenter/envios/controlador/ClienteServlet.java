package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.ClienteDao;
import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.modelo.Cliente;
import co.homecenter.envios.util.LectorFormulario;
import javax.servlet.annotation.WebServlet;

/**
 * Módulo CRUD de clientes. Atiende la ruta {@code /clientes}.
 */
@WebServlet(name = "ClienteServlet", urlPatterns = "/clientes")
public class ClienteServlet extends CrudServlet<Cliente> {

    private static final long serialVersionUID = 1L;

    /** Los DAO no guardan estado, por eso una sola instancia atiende todas las peticiones. */
    private final ClienteDao clienteDao = new ClienteDao();

    @Override
    protected CrudDao<Cliente> getDao() {
        return clienteDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "clientes";
    }

    @Override
    protected String getRutaModulo() {
        return "/clientes";
    }

    @Override
    protected String getNombreEntidad() {
        return "El cliente";
    }

    @Override
    protected Cliente crearEntidadVacia() {
        return new Cliente();
    }

    @Override
    protected Cliente leerFormulario(LectorFormulario formulario, Integer id) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(id);
        leerDatosUsuario(formulario, cliente, id == null);
        cliente.setDireccion(formulario.texto("direccion", "La dirección", 200, true));
        return cliente;
    }
}
