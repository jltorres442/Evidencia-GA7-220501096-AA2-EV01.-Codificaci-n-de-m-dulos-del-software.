package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.LogisticaDao;
import co.homecenter.envios.modelo.Logistica;
import co.homecenter.envios.util.LectorFormulario;
import javax.servlet.annotation.WebServlet;

/**
 * Módulo CRUD del personal de logística. Atiende la ruta {@code /logistica}.
 */
@WebServlet(name = "LogisticaServlet", urlPatterns = "/logistica")
public class LogisticaServlet extends CrudServlet<Logistica> {

    private static final long serialVersionUID = 1L;

    private final LogisticaDao logisticaDao = new LogisticaDao();

    @Override
    protected CrudDao<Logistica> getDao() {
        return logisticaDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "logistica";
    }

    @Override
    protected String getRutaModulo() {
        return "/logistica";
    }

    @Override
    protected String getNombreEntidad() {
        return "El responsable de logística";
    }

    @Override
    protected Logistica crearEntidadVacia() {
        return new Logistica();
    }

    @Override
    protected Logistica leerFormulario(LectorFormulario formulario, Integer id) {
        Logistica logistica = new Logistica();
        logistica.setIdLogistica(id);
        leerDatosUsuario(formulario, logistica, id == null);
        return logistica;
    }
}
