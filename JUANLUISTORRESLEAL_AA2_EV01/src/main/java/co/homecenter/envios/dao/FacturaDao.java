package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Factura;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Factura}.
 */
public class FacturaDao extends BaseDao implements CrudDao<Factura> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Factura (fecha_emision, monto_total, id_pedido) VALUES (?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT f.*, c.nombre AS nombre_cliente FROM Factura f "
            + "INNER JOIN Pedido p ON p.id_pedido = f.id_pedido "
            + "INNER JOIN Cliente c ON c.id_cliente = p.id_cliente ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE f.id_factura = ?";

    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE CAST(f.id_factura AS CHAR) LIKE ? OR CAST(f.id_pedido AS CHAR) LIKE ? OR c.nombre LIKE ? "
            + "ORDER BY f.fecha_emision DESC, f.id_factura DESC";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Factura SET fecha_emision = ?, monto_total = ?, id_pedido = ? WHERE id_factura = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Factura WHERE id_factura = ?";

    @Override
    public int insertar(Factura factura) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(factura.getFechaEmision()));
            sentencia.setBigDecimal(2, factura.getMontoTotal());
            sentencia.setInt(3, factura.getIdPedido());
        });
    }

    @Override
    public Optional<Factura> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearFactura);
    }

    @Override
    public List<Factura> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
            sentencia.setString(3, patron);
        }, this::mapearFactura);
    }

    @Override
    public boolean actualizar(Factura factura) throws DaoException {
        return ejecutarModificacion(SQL_ACTUALIZAR, sentencia -> {
            sentencia.setDate(1, Date.valueOf(factura.getFechaEmision()));
            sentencia.setBigDecimal(2, factura.getMontoTotal());
            sentencia.setInt(3, factura.getIdPedido());
            sentencia.setInt(4, factura.getIdFactura());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Factura mapearFactura(ResultSet resultado) throws SQLException {
        Factura factura = new Factura();
        factura.setIdFactura(resultado.getInt("id_factura"));
        factura.setFechaEmision(resultado.getDate("fecha_emision").toLocalDate());
        factura.setMontoTotal(resultado.getBigDecimal("monto_total"));
        factura.setIdPedido(resultado.getInt("id_pedido"));
        factura.setNombreCliente(resultado.getString("nombre_cliente"));
        return factura;
    }
}
