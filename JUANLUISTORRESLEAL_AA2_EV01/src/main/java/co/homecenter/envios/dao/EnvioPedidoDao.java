package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.EnvioPedido;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Envio_Pedido}.
 */
public class EnvioPedidoDao extends BaseDao implements CrudDao<EnvioPedido> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Envio_Pedido (fecha_envio, direccion_entrega, estado, id_pedido, id_logistica) "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT e.*, c.nombre AS nombre_cliente, l.nombre AS nombre_logistica FROM Envio_Pedido e "
            + "INNER JOIN Pedido p ON p.id_pedido = e.id_pedido "
            + "INNER JOIN Cliente c ON c.id_cliente = p.id_cliente "
            + "INNER JOIN Logistica l ON l.id_logistica = e.id_logistica ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE e.id_envio = ?";

    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE CAST(e.id_envio AS CHAR) LIKE ? OR e.direccion_entrega LIKE ? OR e.estado LIKE ? "
            + "OR c.nombre LIKE ? ORDER BY e.fecha_envio DESC, e.id_envio DESC";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Envio_Pedido SET fecha_envio = ?, direccion_entrega = ?, estado = ?, id_pedido = ?, "
            + "id_logistica = ? WHERE id_envio = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Envio_Pedido WHERE id_envio = ?";

    @Override
    public int insertar(EnvioPedido envio) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(envio.getFechaEnvio()));
            sentencia.setString(2, envio.getDireccionEntrega());
            sentencia.setString(3, envio.getEstado());
            sentencia.setInt(4, envio.getIdPedido());
            sentencia.setInt(5, envio.getIdLogistica());
        });
    }

    @Override
    public Optional<EnvioPedido> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearEnvio);
    }

    @Override
    public List<EnvioPedido> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearEnvio);
    }

    @Override
    public boolean actualizar(EnvioPedido envio) throws DaoException {
        return ejecutarModificacion(SQL_ACTUALIZAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(envio.getFechaEnvio()));
            sentencia.setString(2, envio.getDireccionEntrega());
            sentencia.setString(3, envio.getEstado());
            sentencia.setInt(4, envio.getIdPedido());
            sentencia.setInt(5, envio.getIdLogistica());
            sentencia.setInt(6, envio.getIdEnvio());
        }) > 0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Las incidencias del envío se eliminan en cascada y los transportistas
     * que lo tenían asignado quedan disponibles ({@code id_envio = NULL}).</p>
     */
    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private EnvioPedido mapearEnvio(ResultSet resultado) throws SQLException {
        EnvioPedido envio = new EnvioPedido();
        envio.setIdEnvio(resultado.getInt("id_envio"));
        envio.setFechaEnvio(resultado.getDate("fecha_envio").toLocalDate());
        envio.setDireccionEntrega(resultado.getString("direccion_entrega"));
        envio.setEstado(resultado.getString("estado"));
        envio.setIdPedido(resultado.getInt("id_pedido"));
        envio.setIdLogistica(resultado.getInt("id_logistica"));
        envio.setNombreCliente(resultado.getString("nombre_cliente"));
        envio.setNombreLogistica(resultado.getString("nombre_logistica"));
        return envio;
    }
}
