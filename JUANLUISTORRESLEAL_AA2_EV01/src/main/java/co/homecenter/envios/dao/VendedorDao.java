package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Vendedor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Vendedor}.
 */
public class VendedorDao extends BaseDao implements CrudDao<Vendedor> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Vendedor (numero_identificacion, nombre, email, contrasena, telefono, registro_pedido) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM Vendedor WHERE id_vendedor = ?";

    private static final String SQL_LISTAR =
            "SELECT * FROM Vendedor "
            + "WHERE nombre LIKE ? OR email LIKE ? OR numero_identificacion LIKE ? OR telefono LIKE ? "
            + "ORDER BY nombre";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Vendedor SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, "
            + "registro_pedido = ? WHERE id_vendedor = ?";

    private static final String SQL_ACTUALIZAR_CON_CONTRASENA =
            "UPDATE Vendedor SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, "
            + "registro_pedido = ?, contrasena = ? WHERE id_vendedor = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Vendedor WHERE id_vendedor = ?";

    @Override
    public int insertar(Vendedor vendedor) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, vendedor.getNumeroIdentificacion());
            sentencia.setString(2, vendedor.getNombre());
            sentencia.setString(3, vendedor.getEmail());
            sentencia.setString(4, vendedor.getContrasena());
            sentencia.setString(5, vendedor.getTelefono());
            sentencia.setBoolean(6, vendedor.isRegistroPedido());
        });
    }

    @Override
    public Optional<Vendedor> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearVendedor);
    }

    @Override
    public List<Vendedor> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearVendedor);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Si la contraseña es {@code null} se conserva la actual.</p>
     */
    @Override
    public boolean actualizar(Vendedor vendedor) throws DaoException {
        boolean cambiaContrasena = vendedor.getContrasena() != null;
        String sql = cambiaContrasena ? SQL_ACTUALIZAR_CON_CONTRASENA : SQL_ACTUALIZAR;
        return ejecutarModificacion(sql, sentencia -> {
            int posicion = 1;
            sentencia.setString(posicion++, vendedor.getNumeroIdentificacion());
            sentencia.setString(posicion++, vendedor.getNombre());
            sentencia.setString(posicion++, vendedor.getEmail());
            sentencia.setString(posicion++, vendedor.getTelefono());
            sentencia.setBoolean(posicion++, vendedor.isRegistroPedido());
            if (cambiaContrasena) {
                sentencia.setString(posicion++, vendedor.getContrasena());
            }
            sentencia.setInt(posicion, vendedor.getIdVendedor());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Vendedor mapearVendedor(ResultSet resultado) throws SQLException {
        Vendedor vendedor = new Vendedor();
        vendedor.setIdVendedor(resultado.getInt("id_vendedor"));
        vendedor.setNumeroIdentificacion(resultado.getString("numero_identificacion"));
        vendedor.setNombre(resultado.getString("nombre"));
        vendedor.setEmail(resultado.getString("email"));
        vendedor.setContrasena(resultado.getString("contrasena"));
        vendedor.setTelefono(resultado.getString("telefono"));
        vendedor.setRegistroPedido(resultado.getBoolean("registro_pedido"));
        return vendedor;
    }
}
