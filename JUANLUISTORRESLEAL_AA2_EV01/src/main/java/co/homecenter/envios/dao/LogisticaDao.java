package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Logistica;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Logistica}.
 */
public class LogisticaDao extends BaseDao implements CrudDao<Logistica> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Logistica (numero_identificacion, nombre, email, contrasena, telefono) "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM Logistica WHERE id_logistica = ?";

    private static final String SQL_LISTAR =
            "SELECT * FROM Logistica "
            + "WHERE nombre LIKE ? OR email LIKE ? OR numero_identificacion LIKE ? OR telefono LIKE ? "
            + "ORDER BY nombre";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Logistica SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ? "
            + "WHERE id_logistica = ?";

    private static final String SQL_ACTUALIZAR_CON_CONTRASENA =
            "UPDATE Logistica SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, "
            + "contrasena = ? WHERE id_logistica = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Logistica WHERE id_logistica = ?";

    @Override
    public int insertar(Logistica logistica) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, logistica.getNumeroIdentificacion());
            sentencia.setString(2, logistica.getNombre());
            sentencia.setString(3, logistica.getEmail());
            sentencia.setString(4, logistica.getContrasena());
            sentencia.setString(5, logistica.getTelefono());
        });
    }

    @Override
    public Optional<Logistica> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearLogistica);
    }

    @Override
    public List<Logistica> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearLogistica);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Si la contraseña es {@code null} se conserva la actual.</p>
     */
    @Override
    public boolean actualizar(Logistica logistica) throws DaoException {
        boolean cambiaContrasena = logistica.getContrasena() != null;
        String sql = cambiaContrasena ? SQL_ACTUALIZAR_CON_CONTRASENA : SQL_ACTUALIZAR;
        return ejecutarModificacion(sql, sentencia -> {
            int posicion = 1;
            sentencia.setString(posicion++, logistica.getNumeroIdentificacion());
            sentencia.setString(posicion++, logistica.getNombre());
            sentencia.setString(posicion++, logistica.getEmail());
            sentencia.setString(posicion++, logistica.getTelefono());
            if (cambiaContrasena) {
                sentencia.setString(posicion++, logistica.getContrasena());
            }
            sentencia.setInt(posicion, logistica.getIdLogistica());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Logistica mapearLogistica(ResultSet resultado) throws SQLException {
        Logistica logistica = new Logistica();
        logistica.setIdLogistica(resultado.getInt("id_logistica"));
        logistica.setNumeroIdentificacion(resultado.getString("numero_identificacion"));
        logistica.setNombre(resultado.getString("nombre"));
        logistica.setEmail(resultado.getString("email"));
        logistica.setContrasena(resultado.getString("contrasena"));
        logistica.setTelefono(resultado.getString("telefono"));
        return logistica;
    }
}
