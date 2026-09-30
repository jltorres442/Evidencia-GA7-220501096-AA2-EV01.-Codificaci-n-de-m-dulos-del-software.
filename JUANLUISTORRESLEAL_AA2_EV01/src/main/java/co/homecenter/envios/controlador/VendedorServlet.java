package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.VendedorDao;
import co.homecenter.envios.modelo.Vendedor;
import co.homecenter.envios.util.LectorFormulario;
import javax.servlet.annotation.WebServlet;

/**
 * Módulo CRUD de vendedores. Atiende la ruta {@code /vendedores}.
 */
@WebServlet(name = "VendedorServlet", urlPatterns = "/vendedores")
public class VendedorServlet extends CrudServlet<Vendedor> {

    private static final long serialVersionUID = 1L;

    private final VendedorDao vendedorDao = new VendedorDao();

    @Override
    protected CrudDao<Vendedor> getDao() {
        return vendedorDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "vendedores";
    }

    @Override
    protected String getRutaModulo() {
        return "/vendedores";
    }

    @Override
    protected String getNombreEntidad() {
        return "El vendedor";
    }

    @Override
    protected Vendedor crearEntidadVacia() {
        return new Vendedor();
    }

    @Override
    protected Vendedor leerFormulario(LectorFormulario formulario, Integer id) {
        Vendedor vendedor = new Vendedor();
        vendedor.setIdVendedor(id);
        leerDatosUsuario(formulario, vendedor, id == null);
        vendedor.setRegistroPedido(formulario.casilla("registroPedido"));
        return vendedor;
    }
}
