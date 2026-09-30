package co.homecenter.envios.dao;

import co.homecenter.envios.modelo.Transportista;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de la tabla {@code Transportista}.
 *
 * <p>Las consultas hacen {@code LEFT JOIN} con {@code Envio_Pedido} para
 * mostrar la dirección del envío asignado, si lo hay.</p>
 */
public class TransportistaDao extends BaseDao implements CrudDao<Transportista> {

    private static final String SQL_INSERTAR =
            "INSERT INTO Transportista (numero_identificacion, nombre, email, contrasena, telefono, licencia, id_envio) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECCION =
            "SELECT t.*, e.direccion_entrega FROM Transportista t "
            + "LEFT JOIN Envio_Pedido e ON e.id_envio = t.id_envio ";

    private static final String SQL_BUSCAR_POR_ID =
            SQL_SELECCION + "WHERE t.id_transportista = ?";

    private static final String SQL_LISTAR =
            SQL_SELECCION
            + "WHERE t.nombre LIKE ? OR t.email LIKE ? OR t.numero_identificacion LIKE ? OR t.licencia LIKE ? "
            + "ORDER BY t.nombre";

    private static final String SQL_ACTUALIZAR =
            "UPDATE Transportista SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, "
            + "licencia = ?, id_envio = ? WHERE id_transportista = ?";

    private static final String SQL_ACTUALIZAR_CON_CONTRASENA =
            "UPDATE Transportista SET numero_identificacion = ?, nombre = ?, email = ?, telefono = ?, "
            + "licencia = ?, id_envio = ?, contrasena = ? WHERE id_transportista = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM Transportista WHERE id_transportista = ?";

    @Override
    public int insertar(Transportista transportista) throws DaoException {
        return ejecutarInsercion(SQL_INSERTAR, sentencia -> {
            sentencia.setString(1, transportista.getNumeroIdentificacion());
            sentencia.setString(2, transportista.getNombre());
            sentencia.setString(3, transportista.getEmail());
            sentencia.setString(4, transportista.getContrasena());
            sentencia.setString(5, transportista.getTelefono());
            sentencia.setString(6, transportista.getLicencia());
            asignarEnteroOpcional(sentencia, 7, transportista.getIdEnvio());
        });
    }

    @Override
    public Optional<Transportista> buscarPorId(int id) throws DaoException {
        return consultarUno(SQL_BUSCAR_POR_ID, sentencia -> sentencia.setInt(1, id), this::mapearTransportista);
    }

    @Override
    public List<Transportista> listar(String filtro) throws DaoException {
        String patron = patronBusqueda(filtro);
        return consultarLista(SQL_LISTAR, sentencia -> {
            for (int posicion = 1; posicion <= 4; posicion++) {
                sentencia.setString(posicion, patron);
            }
        }, this::mapearTransportista);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Si la contraseña es {@code null} se conserva la actual.</p>
     */
    @Override
    public boolean actualizar(Transportista transportista) throws DaoException {
        boolean cambiaContrasena = transportista.getContrasena() != null;
        String sql = cambiaContrasena ? SQL_ACTUALIZAR_CON_CONTRASENA : SQL_ACTUALIZAR;
        return ejecutarModificacion(sql, sentencia -> {
            int posicion = 1;
            sentencia.setString(posicion++, transportista.getNumeroIdentificacion());
            sentencia.setString(posicion++, transportista.getNombre());
            sentencia.setString(posicion++, transportista.getEmail());
            sentencia.setString(posicion++, transportista.getTelefono());
            sentencia.setString(posicion++, transportista.getLicencia());
            asignarEnteroOpcional(sentencia, posicion++, transportista.getIdEnvio());
            if (cambiaContrasena) {
                sentencia.setString(posicion++, transportista.getContrasena());
            }
            sentencia.setInt(posicion, transportista.getIdTransportista());
        }) > 0;
    }

    @Override
    public boolean eliminar(int id) throws DaoException {
        return ejecutarModificacion(SQL_ELIMINAR, sentencia -> sentencia.setInt(1, id)) > 0;
    }

    private Transportista mapearTransportista(ResultSet resultado) throws SQLException {
        Transportista transportista = new Transportista();
        transportista.setIdTransportista(resultado.getInt("id_transportista"));
        transportista.setNumeroIdentificacion(resultado.getString("numero_identificacion"));
        transportista.setNombre(resultado.getString("nombre"));
        transportista.setEmail(resultado.getString("email"));
        transportista.setContrasena(resultado.getString("contrasena"));
        transportista.setTelefono(resultado.getString("telefono"));
        transportista.setLicencia(resultado.getString("licencia"));
        transportista.setIdEnvio(leerEnteroOpcional(resultado, "id_envio"));
        transportista.setDireccionEnvio(resultado.getString("direccion_entrega"));
        return transportista;
    }
}
