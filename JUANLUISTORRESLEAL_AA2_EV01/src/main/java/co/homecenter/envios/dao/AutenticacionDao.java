package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Rol;
import co.homecenter.envios.modelo.UsuarioSesion;
import co.homecenter.envios.util.SeguridadContrasena;
import java.util.Optional;

/**
 * Valida las credenciales de inicio de sesión.
 *
 * <p>Como cada rol tiene su propia tabla, la consulta une las cuatro tablas
 * de usuarios con {@code UNION ALL} y busca el correo en todas ellas.</p>
 */
public class AutenticacionDao extends BaseDao {

    private static final String SQL_BUSCAR_POR_EMAIL =
            "SELECT id_cliente AS id, nombre, email, contrasena, 'CLIENTE' AS rol "
            + "FROM Cliente WHERE email = ? "
            + "UNION ALL "
            + "SELECT id_vendedor, nombre, email, contrasena, 'VENDEDOR' FROM Vendedor WHERE email = ? "
            + "UNION ALL "
            + "SELECT id_logistica, nombre, email, contrasena, 'LOGISTICA' FROM Logistica WHERE email = ? "
            + "UNION ALL "
            + "SELECT id_transportista, nombre, email, contrasena, 'TRANSPORTISTA' "
            + "FROM Transportista WHERE email = ?";

    /**
     * Fila intermedia con el hash, que no debe salir de esta clase.
     */
    private static final class Credencial {
        private final UsuarioSesion usuario;
        private final String hashContrasena;

        private Credencial(UsuarioSesion usuario, String hashContrasena) {
            this.usuario = usuario;
            this.hashContrasena = hashContrasena;
        }
    }

    /**
     * Verifica el correo y la contraseña de un usuario.
     *
     * @param email           correo digitado (se compara en minúsculas)
     * @param contrasenaPlana contraseña digitada
     * @return datos del usuario si las credenciales son válidas; vacío en caso contrario
     * @throws DaoException si falla la consulta
     */
    public Optional<UsuarioSesion> autenticar(String email, String contrasenaPlana) throws DaoException {
        String emailNormalizado = email.trim().toLowerCase();
        Optional<Credencial> credencial = consultarUno(SQL_BUSCAR_POR_EMAIL, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, emailNormalizado);
            }
        }, resultado -> new Credencial(
                new UsuarioSesion(resultado.getInt("id"), resultado.getString("nombre"),
                        resultado.getString("email"), Rol.valueOf(resultado.getString("rol"))),
                resultado.getString("contrasena")));

        return credencial
                .filter(encontrada -> SeguridadContrasena.verificar(contrasenaPlana, encontrada.hashContrasena))
                .map(encontrada -> encontrada.usuario);
    }
}
