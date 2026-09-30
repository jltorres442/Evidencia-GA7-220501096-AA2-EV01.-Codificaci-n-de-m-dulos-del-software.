package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.dao.EstadisticaDao;
import co.homecenter.envios.dao.PedidoDao;
import co.homecenter.envios.modelo.UsuarioSesion;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Panel principal después de iniciar sesión. Atiende {@code /inicio}.
 *
 * <p>El contenido depende del rol: el personal interno ve indicadores y los
 * pedidos recientes; el cliente ve el historial de sus propios pedidos.</p>
 */
@WebServlet(name = "InicioServlet", urlPatterns = "/inicio")
public class InicioServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    /** Cantidad de pedidos recientes que se muestran en el panel. */
    private static final int CANTIDAD_PEDIDOS_RECIENTES = 5;

    private final EstadisticaDao estadisticaDao = new EstadisticaDao();
    private final PedidoDao pedidoDao = new PedidoDao();

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        UsuarioSesion usuario = obtenerUsuarioSesion(solicitud);
        try {
            if (usuario.getRol().isPersonalInterno()) {
                solicitud.setAttribute("indicadores", estadisticaDao.contarIndicadores());
                solicitud.setAttribute("pedidos", pedidoDao.listarRecientes(CANTIDAD_PEDIDOS_RECIENTES));
            } else {
                solicitud.setAttribute("pedidos", pedidoDao.listarPorCliente(usuario.getId()));
            }
        } catch (DaoException excepcion) {
            solicitud.setAttribute("pedidos", Collections.emptyList());
            solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, excepcion.getMessage());
        }
        mostrarVista(solicitud, respuesta, "inicio.jsp");
    }
}
