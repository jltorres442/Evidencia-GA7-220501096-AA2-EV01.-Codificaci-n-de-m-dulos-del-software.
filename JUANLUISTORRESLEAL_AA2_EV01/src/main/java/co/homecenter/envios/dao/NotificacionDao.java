package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Notificacion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Notificacion}.
 */
public class NotificacionDao extends BaseDao implements CrudDao<Notificacion> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Notificacion (mensaje, fecha_hora, id_transportista) VALUES (?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT n.*, t.nombre AS nombre_transportista FROM Notificacion n "
            + "INNER JOIN Transportista t ON t.id_transportista = n.id_transportista ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE n.id_notificacion = ?";

    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE n.mensaje LIKE ? OR t.nombre LIKE ? "
            + "ORDER BY n.fecha_hora DESC, n.id_notificacion DESC";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Notificacion SET mensaje = ?, fecha_hora = ?, id_transportista = ? WHERE id_notificacion = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Notificacion WHERE id_notificacion = ?";

    @Override
    public int insertar(Notificacion notificacion) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, notificacion.getMensaje());
            sentencia.setTimestamp(2, Timestamp.valueOf(notificacion.getFechaHora()));
            sentencia.setInt(3, notificacion.getIdTransportista());
        });
    }

    @Override
    public Optional<Notificacion> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearNotificacion);
    }

    @Override
    public List<Notificacion> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
        }, this::mapearNotificacion);
    }

    @Override
    public boolean actualizar(Notificacion notificacion) throws DaoException {
        return ejecutarModificacion(SQL_ACTUALIZAR, sentencia -> {
            sentencia.setString(1, notificacion.getMensaje());
            sentencia.setTimestamp(2, Timestamp.valueOf(notificacion.getFechaHora()));
            sentencia.setInt(3, notificacion.getIdTransportista());
            sentencia.setInt(4, notificacion.getIdNotificacion());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Notificacion mapearNotificacion(ResultSet resultado) throws SQLException {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(resultado.getInt("id_notificacion"));
        notificacion.setMensaje(resultado.getString("mensaje"));
        notificacion.setFechaHora(resultado.getTimestamp("fecha_hora").toLocalDateTime());
        notificacion.setIdTransportista(resultado.getInt("id_transportista"));
        notificacion.setNombreTransportista(resultado.getString("nombre_transportista"));
        return notificacion;
    }
}
