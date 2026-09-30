package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.dao.NotificacionDao;
import co.homecenter.envios.dao.TransportistaDao;
import co.homecenter.envios.modelo.Notificacion;
import co.homecenter.envios.util.LectorFormulario;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Módulo CRUD de notificaciones a transportistas. Atiende la ruta {@code /notificaciones}.
 */
@WebServlet(name = "NotificacionServlet", urlPatterns = "/notificaciones")
public class NotificacionServlet extends CrudServlet<Notificacion> {

    private static final long serialVersionUID = 1L;

    private final NotificacionDao notificacionDao = new NotificacionDao();
    private final TransportistaDao transportistaDao = new TransportistaDao();

    @Override
    protected CrudDao<Notificacion> getDao() {
        return notificacionDao;
    }

    @Override
    protected String getCarpetaVistas() {
        return "notificaciones";
    }

    @Override
    protected String getRutaModulo() {
        return "/notificaciones";
    }

    @Override
    protected String getNombreEntidad() {
        return "La notificación";
    }

    /** Una notificación nueva se propone con la fecha y hora actuales (sin segundos). */
    @Override
    protected Notificacion crearEntidadVacia() {
        Notificacion notificacion = new Notificacion();
        notificacion.setFechaHora(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        return notificacion;
    }

    @Override
    protected Notificacion leerFormulario(LectorFormulario formulario, Integer id) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(id);
        notificacion.setMensaje(formulario.texto("mensaje", "El mensaje", 2000, true));
        notificacion.setFechaHora(formulario.fechaHora("fechaHora", "La fecha y hora", true));
        notificacion.setIdTransportista(formulario.entero("idTransportista", "El transportista", true));
        return notificacion;
    }

    @Override
    protected void cargarCatalogos(HttpServletRequest solicitud) throws DaoException {
        solicitud.setAttribute("transportistas", transportistaDao.listar(null));
    }
}
