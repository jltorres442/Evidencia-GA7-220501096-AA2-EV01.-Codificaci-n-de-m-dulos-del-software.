package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Pedido;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Pedido}.
 *
 * <p>Las consultas unen {@code Cliente} y {@code Vendedor} para devolver sus
 * nombres junto con cada pedido.</p>
 */
public class PedidoDao extends BaseDao implements CrudDao<Pedido> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Pedido (fecha, total, estado, id_cliente, id_vendedor) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT p.*, c.nombre AS nombre_cliente, v.nombre AS nombre_vendedor FROM Pedido p "
            + "INNER JOIN Cliente c ON c.id_cliente = p.id_cliente "
            + "INNER JOIN Vendedor v ON v.id_vendedor = p.id_vendedor ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE p.id_pedido = ?";

    /** Busca por número de pedido, estado, cliente o vendedor. */
    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE CAST(p.id_pedido AS CHAR) LIKE ? OR p.estado LIKE ? OR c.nombre LIKE ? OR v.nombre LIKE ? "
            + "ORDER BY p.fecha DESC, p.id_pedido DESC";

    private static final String SQL_LISTAR_POR_CLIENTE =
            SQL_SELECCION + "WHERE p.id_cliente = ? ORDER BY p.fecha DESC, p.id_pedido DESC";

    private static final String SQL_LISTAR_RECIENTES =
            SQL_SELECCION + "ORDER BY p.fecha DESC, p.id_pedido DESC LIMIT ?";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Pedido SET fecha = ?, total = ?, estado = ?, id_cliente = ?, id_vendedor = ? "
            + "WHERE id_pedido = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Pedido WHERE id_pedido = ?";

    @Override
    public int insertar(Pedido pedido) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(pedido.getFecha()));
            sentencia.setBigDecimal(2, pedido.getTotal());
            sentencia.setString(3, pedido.getEstado());
            sentencia.setInt(4, pedido.getIdCliente());
            sentencia.setInt(5, pedido.getIdVendedor());
        });
    }

    @Override
    public Optional<Pedido> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearPedido);
    }

    @Override
    public List<Pedido> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearPedido);
    }

    /**
     * Consulta los pedidos de un cliente, para su portal personal.
     *
     * @param idCliente llave primaria del cliente
     * @return pedidos del cliente, del más reciente al más antiguo
     * @throws DaoException si falla la consulta
     */
    public List<Pedido> listarPorCliente(int idCliente) throws DaoException {
        return consultarLista(SQL_LISTAR_POR_CLIENTE, sentencia -> sentencia.setInt(1, idCliente),
                this::mapearPedido);
    }

    /**
     * Consulta los pedidos más recientes para el panel de inicio.
     *
     * @param cantidad número máximo de pedidos
     * @return pedidos recientes
     * @throws DaoException si falla la consulta
     */
    public List<Pedido> listarRecientes(int cantidad) throws DaoException {
        return consultarLista(SQL_LISTAR_RECIENTES, sentencia -> sentencia.setInt(1, cantidad),
                this::mapearPedido);
    }

    @Override
    public boolean actualizar(Pedido pedido) throws DaoException {
        return ejecutarModificacion(SQL_ACTUALIZAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(pedido.getFecha()));
            sentencia.setBigDecimal(2, pedido.getTotal());
            sentencia.setString(3, pedido.getEstado());
            sentencia.setInt(4, pedido.getIdCliente());
            sentencia.setInt(5, pedido.getIdVendedor());
            sentencia.setInt(6, pedido.getIdPedido());
        }) > 0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Por la regla {@code ON DELETE CASCADE}, al eliminar un pedido también
     * se eliminan su factura, su envío y las incidencias de ese envío.</p>
     */
    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Pedido mapearPedido(ResultSet resultado) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("id_pedido"));
        pedido.setFecha(resultado.getDate("fecha").toLocalDate());
        pedido.setTotal(resultado.getBigDecimal("total"));
        pedido.setEstado(resultado.getString("estado"));
        pedido.setIdCliente(resultado.getInt("id_cliente"));
        pedido.setIdVendedor(resultado.getInt("id_vendedor"));
        pedido.setNombreCliente(resultado.getString("nombre_cliente"));
        pedido.setNombreVendedor(resultado.getString("nombre_vendedor"));
        return pedido;
    }
}
