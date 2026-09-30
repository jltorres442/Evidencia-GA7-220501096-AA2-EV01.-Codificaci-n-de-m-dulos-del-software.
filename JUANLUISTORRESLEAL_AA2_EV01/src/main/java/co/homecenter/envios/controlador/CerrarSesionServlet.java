package co.homecenter.envios.controlador;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Cierra la sesión del usuario. Atiende {@code /cerrar-sesion}.
 *
 * <p>Solo acepta POST (con token CSRF) para que un enlace malicioso no pueda
 * cerrar la sesión del usuario sin su consentimiento.</p>
 */
@WebServlet(name = "CerrarSesionServlet", urlPatterns = "/cerrar-sesion")
public class CerrarSesionServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta) throws IOException {
        HttpSession sesion = solicitud.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        guardarMensajeExito(solicitud, "Cerró sesión correctamente.");
        redirigir(solicitud, respuesta, "/login");
    }
}
