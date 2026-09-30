package co.homecenter.envios.controlador;

import co.homecenter.envios.modelo.Usuario;
import co.homecenter.envios.modelo.UsuarioSesion;
import co.homecenter.envios.util.LectorFormulario;
import co.homecenter.envios.util.SeguridadContrasena;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Superclase de todos los servlets de la aplicación.
 *
 * <p>Reúne operaciones que todos los controladores necesitan: mostrar una
 * vista JSP, redirigir, publicar mensajes "flash" (que sobreviven a una
 * redirección) y leer los datos comunes de un {@link Usuario}.</p>
 */
public abstract class ServletBase extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Carpeta protegida donde viven las vistas: el navegador no puede abrirlas directamente. */
    protected static final String CARPETA_VISTAS = "/WEB-INF/vistas/";

    /** Atributo de sesión con el {@link UsuarioSesion} autenticado. */
    public static final String ATRIBUTO_USUARIO = "usuarioSesion";

    /** Atributos para mensajes de retroalimentación mostrados en la parte superior de la vista. */
    public static final String ATRIBUTO_MENSAJE_EXITO = "mensajeExito";
    public static final String ATRIBUTO_MENSAJE_ERROR = "mensajeError";

    /**
     * Reenvía la petición a una vista JSP (el navegador conserva la URL).
     *
     * <p>Antes del reenvío mueve los mensajes flash de la sesión a la
     * petición, para que se muestren una sola vez.</p>
     *
     * @param solicitud  petición actual
     * @param respuesta  respuesta actual
     * @param vista      ruta relativa a {@code /WEB-INF/vistas/}, p. ej. {@code "clientes/lista.jsp"}
     * @throws ServletException si la vista falla al generarse
     * @throws IOException      si hay un error de escritura
     */
    protected void mostrarVista(HttpServletRequest solicitud, HttpServletResponse respuesta, String vista)
            throws ServletException, IOException {
        trasladarMensajeFlash(solicitud, ATRIBUTO_MENSAJE_EXITO);
        trasladarMensajeFlash(solicitud, ATRIBUTO_MENSAJE_ERROR);
        solicitud.getRequestDispatcher(CARPETA_VISTAS + vista).forward(solicitud, respuesta);
    }

    /**
     * Redirige el navegador a otra ruta de la aplicación (código HTTP 302).
     *
     * <p>Se usa después de un POST exitoso (patrón Post/Redirect/Get) para que
     * al recargar la página no se reenvíe el formulario.</p>
     *
     * @param solicitud petición actual
     * @param respuesta respuesta actual
     * @param ruta      ruta interna, p. ej. {@code "/clientes"}
     * @throws IOException si no se puede enviar la redirección
     */
    protected void redirigir(HttpServletRequest solicitud, HttpServletResponse respuesta, String ruta)
            throws IOException {
        respuesta.sendRedirect(solicitud.getContextPath() + ruta);
    }

    /**
     * Guarda un mensaje de éxito que se mostrará en la siguiente vista.
     *
     * @param solicitud petición actual
     * @param mensaje   texto del mensaje
     */
    protected void guardarMensajeExito(HttpServletRequest solicitud, String mensaje) {
        solicitud.getSession().setAttribute(ATRIBUTO_MENSAJE_EXITO, mensaje);
    }

    /**
     * Guarda un mensaje de error que se mostrará en la siguiente vista.
     *
     * @param solicitud petición actual
     * @param mensaje   texto del mensaje
     */
    protected void guardarMensajeError(HttpServletRequest solicitud, String mensaje) {
        solicitud.getSession().setAttribute(ATRIBUTO_MENSAJE_ERROR, mensaje);
    }

    /**
     * Obtiene el usuario autenticado de la sesión.
     *
     * @param solicitud petición actual
     * @return usuario en sesión, o {@code null} si no ha iniciado sesión
     */
    protected UsuarioSesion obtenerUsuarioSesion(HttpServletRequest solicitud) {
        HttpSession sesion = solicitud.getSession(false);
        return sesion == null ? null : (UsuarioSesion) sesion.getAttribute(ATRIBUTO_USUARIO);
    }

    /**
     * Lee el parámetro {@code id} de la URL o del formulario.
     *
     * @param solicitud petición actual
     * @return identificador numérico, o {@code null} si no llegó o no es un número
     */
    protected Integer leerIdentificador(HttpServletRequest solicitud) {
        String valor = solicitud.getParameter("id");
        if (valor == null) {
            return null;
        }
        try {
            return Integer.valueOf(valor.trim());
        } catch (NumberFormatException formatoInvalido) {
            return null;
        }
    }

    /**
     * Lee el parámetro {@code accion} que indica qué operación ejecutar.
     *
     * @param solicitud      petición actual
     * @param accionPorDefecto valor si el parámetro no llegó
     * @return acción solicitada
     */
    protected String leerAccion(HttpServletRequest solicitud, String accionPorDefecto) {
        String accion = solicitud.getParameter("accion");
        return (accion == null || accion.isBlank()) ? accionPorDefecto : accion.trim();
    }

    /**
     * Lee y valida los campos comunes de cualquier {@link Usuario}: número de
     * identificación, nombre, correo, teléfono y contraseña (con confirmación).
     *
     * <p>Si se digitó una contraseña válida, se guarda su hash en el objeto;
     * si se dejó vacía durante una edición, la contraseña queda en
     * {@code null} para que el DAO conserve la actual.</p>
     *
     * @param formulario lector del formulario enviado
     * @param usuario    objeto a llenar
     * @param esNuevo    {@code true} si se está creando el registro
     */
    protected void leerDatosUsuario(LectorFormulario formulario, Usuario usuario, boolean esNuevo) {
        usuario.setNumeroIdentificacion(formulario.identificacion("numeroIdentificacion", true));
        usuario.setNombre(formulario.texto("nombre", "El nombre", 100, true));
        usuario.setEmail(formulario.email("email", true));
        usuario.setTelefono(formulario.telefono("telefono", true));

        String contrasena = formulario.contrasena("contrasena", esNuevo);
        String confirmacion = formulario.contrasena("confirmarContrasena", false);
        if (contrasena != null && !contrasena.equals(confirmacion)) {
            formulario.registrarError("confirmarContrasena", "Las contraseñas no coinciden.");
        }
        usuario.setContrasena(contrasena == null ? null : SeguridadContrasena.generarHash(contrasena));
    }

    /**
     * Mueve un atributo de la sesión a la petición y lo elimina de la sesión.
     *
     * @param solicitud petición actual
     * @param atributo  nombre del atributo
     */
    private void trasladarMensajeFlash(HttpServletRequest solicitud, String atributo) {
        HttpSession sesion = solicitud.getSession(false);
        if (sesion == null || sesion.getAttribute(atributo) == null) {
            return;
        }
        if (solicitud.getAttribute(atributo) == null) {
            solicitud.setAttribute(atributo, sesion.getAttribute(atributo));
        }
        sesion.removeAttribute(atributo);
    }
}
