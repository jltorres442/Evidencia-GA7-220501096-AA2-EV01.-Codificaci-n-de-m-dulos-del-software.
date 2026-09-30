package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.CrudDao;
import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.util.LectorFormulario;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador genérico con el flujo completo de un módulo CRUD.
 *
 * <p>Aplica el patrón <em>Template Method</em>: esta clase define el
 * algoritmo (listar, mostrar formulario, guardar, actualizar, eliminar) y
 * cada subclase solo implementa lo que cambia entre tablas, como la lectura
 * del formulario o las listas desplegables.</p>
 *
 * <h2>Rutas atendidas (ejemplo con el módulo {@code /clientes})</h2>
 * <table>
 *   <caption>Operaciones del módulo</caption>
 *   <tr><th>Método</th><th>URL</th><th>Operación</th></tr>
 *   <tr><td>GET</td><td>/clientes?q=texto</td><td>Consultar (listar y buscar)</td></tr>
 *   <tr><td>GET</td><td>/clientes?accion=nuevo</td><td>Formulario de creación</td></tr>
 *   <tr><td>GET</td><td>/clientes?accion=editar&amp;id=5</td><td>Formulario de edición</td></tr>
 *   <tr><td>POST</td><td>/clientes (accion=guardar)</td><td>Insertar</td></tr>
 *   <tr><td>POST</td><td>/clientes (accion=actualizar)</td><td>Actualizar</td></tr>
 *   <tr><td>POST</td><td>/clientes (accion=eliminar)</td><td>Eliminar</td></tr>
 * </table>
 *
 * <p>Las operaciones que modifican datos solo se aceptan por POST, así un
 * enlace o un buscador no puede borrar información por accidente.</p>
 *
 * @param <T> tipo de la entidad administrada
 */
public abstract class CrudServlet<T> extends ServletBase {

    private static final long serialVersionUID = 1L;

    private static final String ACCION_LISTAR = "listar";
    private static final String ACCION_NUEVO = "nuevo";
    private static final String ACCION_EDITAR = "editar";
    private static final String ACCION_GUARDAR = "guardar";
    private static final String ACCION_ACTUALIZAR = "actualizar";
    private static final String ACCION_ELIMINAR = "eliminar";

    // ------------------------------------------------------------------
    // Métodos que cada módulo debe implementar
    // ------------------------------------------------------------------

    /** @return DAO que ejecuta las sentencias SQL del módulo */
    protected abstract CrudDao<T> getDao();

    /** @return carpeta de las vistas dentro de {@code /WEB-INF/vistas/}, p. ej. {@code "clientes"} */
    protected abstract String getCarpetaVistas();

    /** @return ruta URL del módulo, p. ej. {@code "/clientes"} */
    protected abstract String getRutaModulo();

    /** @return nombre singular de la entidad para los mensajes, p. ej. {@code "El cliente"} */
    protected abstract String getNombreEntidad();

    /** @return instancia vacía para el formulario de creación */
    protected abstract T crearEntidadVacia();

    /**
     * Construye la entidad a partir de los datos del formulario.
     *
     * @param formulario lector que convierte y valida los parámetros
     * @param id         llave primaria (solo al actualizar; {@code null} al crear)
     * @return entidad con los datos digitados
     */
    protected abstract T leerFormulario(LectorFormulario formulario, Integer id);

    /**
     * Carga en la petición las listas que necesita el formulario (por
     * ejemplo, clientes y vendedores para el formulario de pedidos). Por
     * defecto no carga nada.
     *
     * @param solicitud petición actual
     * @throws DaoException si falla alguna consulta
     */
    protected void cargarCatalogos(HttpServletRequest solicitud) throws DaoException {
        // Los módulos sin llaves foráneas no necesitan catálogos.
    }

    // ------------------------------------------------------------------
    // Atención de peticiones HTTP
    // ------------------------------------------------------------------

    /**
     * Atiende las peticiones GET: consultas y formularios.
     */
    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String accion = leerAccion(solicitud, ACCION_LISTAR);
        switch (accion) {
            case ACCION_NUEVO:
                mostrarFormulario(solicitud, respuesta, crearEntidadVacia(), true);
                break;
            case ACCION_EDITAR:
                mostrarFormularioEdicion(solicitud, respuesta);
                break;
            default:
                listar(solicitud, respuesta);
                break;
        }
    }

    /**
     * Atiende las peticiones POST: inserción, actualización y eliminación.
     */
    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String accion = leerAccion(solicitud, "");
        switch (accion) {
            case ACCION_GUARDAR:
                guardar(solicitud, respuesta, false);
                break;
            case ACCION_ACTUALIZAR:
                guardar(solicitud, respuesta, true);
                break;
            case ACCION_ELIMINAR:
                eliminar(solicitud, respuesta);
                break;
            default:
                respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
                break;
        }
    }

    // ------------------------------------------------------------------
    // Operaciones CRUD
    // ------------------------------------------------------------------

    /**
     * Consulta los registros (filtrados por el parámetro {@code q}) y muestra la tabla.
     */
    private void listar(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String filtro = solicitud.getParameter("q");
        try {
            solicitud.setAttribute("registros", getDao().listar(filtro));
        } catch (DaoException excepcion) {
            solicitud.setAttribute("registros", Collections.emptyList());
            solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, excepcion.getMessage());
        }
        solicitud.setAttribute("filtro", filtro);
        mostrarVista(solicitud, respuesta, getCarpetaVistas() + "/lista.jsp");
    }

    /**
     * Busca el registro indicado por {@code id} y muestra el formulario con sus datos.
     */
    private void mostrarFormularioEdicion(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        Integer id = leerIdentificador(solicitud);
        if (id == null) {
            redirigir(solicitud, respuesta, getRutaModulo());
            return;
        }
        try {
            Optional<T> registro = getDao().buscarPorId(id);
            if (registro.isPresent()) {
                mostrarFormulario(solicitud, respuesta, registro.get(), false);
            } else {
                guardarMensajeError(solicitud, getNombreEntidad() + " #" + id + " no existe.");
                redirigir(solicitud, respuesta, getRutaModulo());
            }
        } catch (DaoException excepcion) {
            guardarMensajeError(solicitud, excepcion.getMessage());
            redirigir(solicitud, respuesta, getRutaModulo());
        }
    }

    /**
     * Valida el formulario y ejecuta INSERT o UPDATE según corresponda.
     *
     * @param esActualizacion {@code true} para UPDATE, {@code false} para INSERT
     */
    private void guardar(HttpServletRequest solicitud, HttpServletResponse respuesta, boolean esActualizacion)
            throws ServletException, IOException {
        Integer id = esActualizacion ? leerIdentificador(solicitud) : null;
        if (esActualizacion && id == null) {
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el identificador del registro");
            return;
        }

        LectorFormulario formulario = new LectorFormulario(solicitud);
        T entidad = leerFormulario(formulario, id);

        if (formulario.tieneErrores()) {
            solicitud.setAttribute("errores", formulario.getErrores());
            solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, "Revise los campos marcados en rojo.");
            mostrarFormulario(solicitud, respuesta, entidad, !esActualizacion);
            return;
        }

        try {
            if (esActualizacion) {
                if (getDao().actualizar(entidad)) {
                    guardarMensajeExito(solicitud, getNombreEntidad() + " #" + id + " se actualizó correctamente.");
                } else {
                    guardarMensajeError(solicitud, getNombreEntidad() + " #" + id + " ya no existe.");
                }
            } else {
                int idGenerado = getDao().insertar(entidad);
                guardarMensajeExito(solicitud, getNombreEntidad() + " #" + idGenerado + " se registró correctamente.");
            }
            redirigir(solicitud, respuesta, getRutaModulo());
        } catch (DaoException excepcion) {
            // Se vuelve a mostrar el formulario con los datos digitados para no perderlos.
            solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, excepcion.getMessage());
            mostrarFormulario(solicitud, respuesta, entidad, !esActualizacion);
        }
    }

    /**
     * Elimina el registro indicado por {@code id}.
     */
    private void eliminar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws IOException {
        Integer id = leerIdentificador(solicitud);
        if (id == null) {
            respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el identificador del registro");
            return;
        }
        try {
            if (getDao().eliminar(id)) {
                guardarMensajeExito(solicitud, getNombreEntidad() + " #" + id + " se eliminó correctamente.");
            } else {
                guardarMensajeError(solicitud, getNombreEntidad() + " #" + id + " ya no existe.");
            }
        } catch (DaoException excepcion) {
            guardarMensajeError(solicitud, excepcion.getMessage());
        }
        redirigir(solicitud, respuesta, getRutaModulo());
    }

    /**
     * Prepara los atributos del formulario y muestra la vista.
     *
     * @param registro entidad a mostrar
     * @param esNuevo  {@code true} si el formulario es de creación
     */
    private void mostrarFormulario(HttpServletRequest solicitud, HttpServletResponse respuesta,
                                   T registro, boolean esNuevo) throws ServletException, IOException {
        try {
            cargarCatalogos(solicitud);
        } catch (DaoException excepcion) {
            solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, excepcion.getMessage());
        }
        solicitud.setAttribute("registro", registro);
        solicitud.setAttribute("esNuevo", esNuevo);
        mostrarVista(solicitud, respuesta, getCarpetaVistas() + "/formulario.jsp");
    }
}
