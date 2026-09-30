package co.homecenter.envios.configuracion;

import co.homecenter.envios.util.CatalogoEstados;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Se ejecuta una vez cuando Tomcat despliega la aplicación.
 *
 * <p>Publica en el ámbito de aplicación ({@code applicationScope}) las listas
 * de estados, de modo que cualquier JSP pueda usarlas con Expression
 * Language, por ejemplo {@code ${estadosPedido}}.</p>
 */
@WebListener
public class InicializadorAplicacion implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext contexto = evento.getServletContext();
        contexto.setAttribute("estadosPedido", CatalogoEstados.ESTADOS_PEDIDO);
        contexto.setAttribute("estadosEnvio", CatalogoEstados.ESTADOS_ENVIO);
        contexto.setAttribute("estadosIncidencia", CatalogoEstados.ESTADOS_INCIDENCIA);
        contexto.log("Homecenter Envíos: catálogos de estados publicados.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        // No hay recursos que liberar: las conexiones se cierran en cada operación.
    }
}
