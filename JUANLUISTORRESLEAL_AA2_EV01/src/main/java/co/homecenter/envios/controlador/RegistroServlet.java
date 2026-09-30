package co.homecenter.envios.controlador;

import co.homecenter.envios.dao.ClienteDao;
import co.homecenter.envios.dao.DaoException;
import co.homecenter.envios.modelo.Cliente;
import co.homecenter.envios.util.LectorFormulario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Registro público de nuevos clientes. Atiende {@code /registro}.
 *
 * <ul>
 *   <li><strong>GET</strong>: muestra el formulario "Crear cuenta".</li>
 *   <li><strong>POST</strong>: valida los datos e inserta el cliente.</li>
 * </ul>
 */
@WebServlet(name = "RegistroServlet", urlPatterns = "/registro")
public class RegistroServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    private final ClienteDao clienteDao = new ClienteDao();

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        mostrarVista(solicitud, respuesta, "registro.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        LectorFormulario formulario = new LectorFormulario(solicitud);
        Cliente cliente = new Cliente();
        leerDatosUsuario(formulario, cliente, true);
        cliente.setDireccion(formulario.texto("direccion", "La dirección", 200, true));

        if (!formulario.casilla("aceptaTerminos")) {
            formulario.registrarError("aceptaTerminos", "Debe aceptar el tratamiento de datos personales.");
        }

        if (formulario.tieneErrores()) {
            solicitud.setAttribute("errores", formulario.getErrores());
            devolverFormulario(solicitud, respuesta, cliente, "Revise los campos marcados en rojo.");
            return;
        }

        try {
            clienteDao.insertar(cliente);
            guardarMensajeExito(solicitud, "Cuenta creada. Ya puede iniciar sesión con su correo.");
            redirigir(solicitud, respuesta, "/login");
        } catch (DaoException excepcion) {
            devolverFormulario(solicitud, respuesta, cliente, excepcion.getMessage());
        }
    }

    /**
     * Vuelve a mostrar el formulario conservando lo que el usuario digitó.
     */
    private void devolverFormulario(HttpServletRequest solicitud, HttpServletResponse respuesta,
                                    Cliente cliente, String mensaje) throws ServletException, IOException {
        // El nombre "cliente" coincide con el <jsp:useBean id="cliente"> de registro.jsp.
        solicitud.setAttribute("cliente", cliente);
        solicitud.setAttribute(ATRIBUTO_MENSAJE_ERROR, mensaje);
        mostrarVista(solicitud, respuesta, "registro.jsp");
    }
}
