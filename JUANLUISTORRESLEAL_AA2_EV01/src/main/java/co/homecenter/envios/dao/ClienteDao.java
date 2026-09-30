package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Cliente}.
 */
public class ClienteDao extends BaseDao implements CrudDao<Cliente> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Cliente (numero_identificacion, nombre, email, contrasena, telefono, direccion) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM Cliente WHERE id_cliente = ?";

    /** Busca por nombre, correo, identificación o teléfono. */
    private static final String SQL_LISTAR =
            "SELECT * FROM Cliente "
            + "WHERE nombre LIKE ? OR email LIKE ? OR numero_identificacion LIKE ? OR telefono LIKE ? "
            + "ORDER BY nombre";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Cliente SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, direccion = ? "
            + "WHERE id_cliente = ?";

    /** Variante usada cuando el usuario también cambia la contraseña. */
    private static final String SQL_ACTUALIZAR_CON_CONTRASENA =
            "UPDATE Cliente SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, direccion = ?, "
            + "contrasena = ? WHERE id_cliente = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Cliente WHERE id_cliente = ?";

    @Override
    public int insertar(Cliente cliente) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, cliente.getNumeroIdentificacion());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getEmail());
            sentencia.setString(4, cliente.getContrasena());
            sentencia.setString(5, cliente.getTelefono());
            sentencia.setString(6, cliente.getDireccion());
        });
    }

    @Override
    public Optional<Cliente> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearCliente);
    }

    @Override
    public List<Cliente> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearCliente);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Si {@link Cliente#getContrasena()} es {@code null} se conserva la
     * contraseña actual; en caso contrario se reemplaza por el nuevo hash.</p>
     */
    @Override
    public boolean actualizar(Cliente cliente) throws DaoException {
        boolean cambiaContrasena = cliente.getContrasena() != null;
        String sql = cambiaContrasena ? SQL_ACTUALIZAR_CON_CONTRASENA : SQL_ACTUALIZAR;
        return ejecutarModificacion(sql, sentencia -> {
            int posicion = 1;
            sentencia.setString(posicion++, cliente.getNumeroIdentificacion());
            sentencia.setString(posicion++, cliente.getNombre());
            sentencia.setString(posicion++, cliente.getEmail());
            sentencia.setString(posicion++, cliente.getTelefono());
            sentencia.setString(posicion++, cliente.getDireccion());
            if (cambiaContrasena) {
                sentencia.setString(posicion++, cliente.getContrasena());
            }
            sentencia.setInt(posicion, cliente.getIdCliente());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    /**
     * Convierte la fila actual en un {@link Cliente}.
     *
     * @param resultado fila de la tabla Cliente
     * @return cliente con sus datos
     * @throws SQLException si alguna columna no existe
     */
    private Cliente mapearCliente(ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(resultado.getInt("id_cliente"));
        cliente.setNumeroIdentificacion(resultado.getString("numero_identificacion"));
        cliente.setNombre(resultado.getString("nombre"));
        cliente.setEmail(resultado.getString("email"));
        cliente.setContrasena(resultado.getString("contrasena"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setDireccion(resultado.getString("direccion"));
        Timestamp fechaRegistro = resultado.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            cliente.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        return cliente;
    }
}
