package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Incidencia;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Incidencia}.
 */
public class IncidenciaDao extends BaseDao implements CrudDao<Incidencia> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Incidencia (descripcion, estado, id_envio) VALUES (?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT i.*, e.direccion_entrega FROM Incidencia i "
            + "INNER JOIN Envio_Pedido e ON e.id_envio = i.id_envio ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE i.id_incidencia = ?";

    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE i.descripcion LIKE ? OR i.estado LIKE ? OR CAST(i.id_envio AS CHAR) LIKE ? "
            + "ORDER BY i.id_incidencia DESC";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Incidencia SET descripcion = ?, estado = ?, id_envio = ? WHERE id_incidencia = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Incidencia WHERE id_incidencia = ?";

    @Override
    public int insertar(Incidencia incidencia) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, incidencia.getDescripcion());
            sentencia.setString(2, incidencia.getEstado());
            sentencia.setInt(3, incidencia.getIdEnvio());
        });
    }

    @Override
    public Optional<Incidencia> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearIncidencia);
    }

    @Override
    public List<Incidencia> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
        }, this::mapearIncidencia);
    }

    @Override
    public boolean actualizar(Incidencia incidencia) throws DaoException {
        return ejecutarModificacion(SQL_ACTUALIZAR, sentencia -> {
            sentencia.setString(1, incidencia.getDescripcion());
            sentencia.setString(2, incidencia.getEstado());
            sentencia.setInt(3, incidencia.getIdEnvio());
            sentencia.setInt(4, incidencia.getIdIncidencia());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Incidencia mapearIncidencia(ResultSet resultado) throws SQLException {
        Incidencia incidencia = new Incidencia();
        incidencia.setIdIncidencia(resultado.getInt("id_incidencia"));
        incidencia.setDescripcion(resultado.getString("descripcion"));
        incidencia.setEstado(resultado.getString("estado"));
        incidencia.setIdEnvio(resultado.getInt("id_envio"));
        incidencia.setDireccionEnvio(resultado.getString("direccion_entrega"));
        return incidencia;
    }
}
