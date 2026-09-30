package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.dao.EnvioPedidoDao;
import co.homecenter.envios.dao.IncidenciaDao;
import co.homecenter.envios.modelo.Incidencia;
import co.homecenter.envios.util.CatalogoEstados;
import co.homecenter.envios.util.LectorFormulario;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Módulo CRUD de incidencias de entrega. Atiende la ruta {@code /incidencias}.
 */
@WebServlet(name = "IncidenciaServlet", urlPatterns = "/incidencias")
public class IncidenciaServlet extends CrudServlet<Incidencia> {

    private static final long serialVersionUID = 1L;

    private final IncidenciaDao incidenciaDao = new IncidenciaDao();
    private final EnvioPedidoDao envioPedidoDao = new EnvioPedidoDao();

    @Override
    protected CrudDao<Incidencia> getDao() {
        return incidenciaDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "incidencias";
    }

    @Override
    protected String getRutaModulo() {
        return "/incidencias";
    }

    @Override
    protected String getNombreEntidad() {
        return "La incidencia";
    }

    @Override
    protected Incidencia crearEntidadVacia() {
        Incidencia incidencia = new Incidencia();
        incidencia.setEstado(CatalogoEstados.ESTADOS_INCIDENCIA.get(0));
        return incidencia;
    }

    @Override
    protected Incidencia leerFormulario(LectorFormulario formulario, Integer id) {
        Incidencia incidencia = new Incidencia();
        incidencia.setIdIncidencia(id);
        incidencia.setDescripcion(formulario.texto("descripcion", "La descripción", 2000, true));
        incidencia.setEstado(formulario.opcion("estado", "El estado", CatalogoEstados.ESTADOS_INCIDENCIA));
        incidencia.setIdEnvio(formulario.entero("idEnvio", "El envío", true));
        return incidencia;
    }

    @Override
    protected void cargarCatalogos(HttpServletRequest solicitud) throws DaoException {
        solicitud.setAttribute("envios", envioPedidoDao.listar(null));
    }
}
