package co.homecenter.envios.filtro;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Fuerza la codificación UTF-8 en peticiones y respuestas, para que tildes
 * y eñes enviadas en los formularios lleguen íntegras a la base de datos.
 *
 * <p>Debe ser el primer filtro de la cadena (ver {@code web.xml}), porque la
 * codificación solo puede fijarse antes de leer el primer parámetro.</p>
 */
public class CodificacionFiltro implements Filter {

    @Override
    public void init(FilterConfig configuracion) {
        // No requiere configuración inicial.
    }

    @Override
    public void doFilter(ServletRequest solicitud, ServletResponse respuesta, FilterChain cadena)
            throws IOException, ServletException {
        solicitud.setCharacterEncoding(StandardCharsets.UTF_8.name());
        respuesta.setCharacterEncoding(StandardCharsets.UTF_8.name());
        cadena.doFilter(solicitud, respuesta);
    }

    @Override
    public void destroy() {
        // No hay recursos que liberar.
    }
}
