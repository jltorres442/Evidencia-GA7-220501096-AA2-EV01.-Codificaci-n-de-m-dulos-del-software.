package co.homecenter.envios.filtro;

import co.homecenter.envios.controlador.ServletBase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Protección contra <em>Cross-Site Request Forgery</em> (CSRF).
 *
 * <p>Cada sesión recibe un token aleatorio que las vistas incluyen como
 * campo oculto {@code tokenCsrf} en todos los formularios POST. Si una
 * petición POST no trae el token correcto, se rechaza: así otro sitio web
 * no puede enviar formularios en nombre del usuario.</p>
 */
public class CsrfFiltro implements Filter {

    /** Nombre del atributo de sesión y del parámetro del formulario. */
    public static final String NOMBRE_TOKEN = "tokenCsrf";

    private static final String RUTA_CERRAR_SESION = "/cerrar-sesion";
    private static final int LONGITUD_TOKEN_BYTES = 32;
    private static final SecureRandom GENERADOR_ALEATORIO = new SecureRandom();

    @Override
    public void init(FilterConfig configuracion) {
        // No requiere configuración inicial.
    }

    @Override
    public void destroy() {
        // No hay recursos que liberar.
    }

    @Override
    public void doFilter(ServletRequest solicitudServlet, ServletResponse respuestaServlet, FilterChain cadena)
            throws IOException, ServletException {
        HttpServletRequest solicitud = (HttpServletRequest) solicitudServlet;
        HttpServletResponse respuesta = (HttpServletResponse) respuestaServlet;

        if ("POST".equalsIgnoreCase(solicitud.getMethod())) {
            HttpSession sesion = solicitud.getSession(false);
            String tokenEsperado = sesion == null ? null : (String) sesion.getAttribute(NOMBRE_TOKEN);
            String tokenRecibido = solicitud.getParameter(NOMBRE_TOKEN);
            if (!tokensCoinciden(tokenEsperado, tokenRecibido)) {
                // Lo más común es que la sesión haya expirado con el formulario abierto.
                HttpSession sesionNueva = solicitud.getSession(true);
                sesionNueva.setAttribute(ServletBase.ATRIBUTO_MENSAJE_ERROR,
                        "Su sesión expiró o el formulario no es válido. Intente de nuevo.");
                String ruta = solicitud.getServletPath();
                // /cerrar-sesion solo acepta POST, por eso se envía al formulario de ingreso.
                String destino = RUTA_CERRAR_SESION.equals(ruta) ? "/login" : ruta;
                respuesta.sendRedirect(solicitud.getContextPath() + destino);
                return;
            }
        } else {
            // En GET se garantiza que exista el token para que las vistas lo usen.
            asegurarToken(solicitud.getSession(true));
        }
        cadena.doFilter(solicitud, respuesta);
    }

    /**
     * Crea el token CSRF de la sesión si aún no existe.
     *
     * @param sesion sesión HTTP
     */
    public static void asegurarToken(HttpSession sesion) {
        if (sesion.getAttribute(NOMBRE_TOKEN) == null) {
            byte[] bytesAleatorios = new byte[LONGITUD_TOKEN_BYTES];
            GENERADOR_ALEATORIO.nextBytes(bytesAleatorios);
            sesion.setAttribute(NOMBRE_TOKEN, Base64.getUrlEncoder().withoutPadding().encodeToString(bytesAleatorios));
        }
    }

    /**
     * Compara los tokens en tiempo constante.
     *
     * @param esperado token guardado en la sesión
     * @param recibido token enviado por el formulario
     * @return {@code true} si ambos existen y son iguales
     */
    private static boolean tokensCoinciden(String esperado, String recibido) {
        if (esperado == null || recibido == null) {
            return false;
        }
        return MessageDigest.isEqual(esperado.getBytes(StandardCharsets.UTF_8),
                recibido.getBytes(StandardCharsets.UTF_8));
    }
}
