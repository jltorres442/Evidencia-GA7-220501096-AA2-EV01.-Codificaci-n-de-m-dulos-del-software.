package co.homecenter.envios.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de las operaciones CRUD (<em>Create, Read, Update, Delete</em>)
 * que implementa cada DAO del sistema.
 *
 * @param <T> tipo de la entidad administrada (Cliente, Pedido, etc.)
 */
public interface CrudDao<T> {

    /**
     * Inserta un nuevo registro (sentencia {@code INSERT}).
     *
     * @param entidad datos a guardar
     * @return llave primaria generada por la base de datos
     * @throws DaoException si se viola una restricción o falla la conexión
     */
    int insertar(T entidad) throws DaoException;

    /**
     * Consulta un registro por su llave primaria (sentencia {@code SELECT}).
     *
     * @param id llave primaria
     * @return el registro, o {@link Optional#empty()} si no existe
     * @throws DaoException si falla la conexión
     */
    Optional<T> buscarPorId(int id) throws DaoException;

    /**
     * Consulta todos los registros, opcionalmente filtrados por un texto.
     *
     * @param filtro texto a buscar en las columnas principales; si es
     *               {@code null} o vacío se devuelven todos los registros
     * @return lista de registros (nunca {@code null})
     * @throws DaoException si falla la conexión
     */
    List<T> listar(String filtro) throws DaoException;

    /**
     * Actualiza un registro existente (sentencia {@code UPDATE}).
     *
     * @param entidad datos nuevos; debe tener la llave primaria asignada
     * @return {@code true} si se modificó una fila
     * @throws DaoException si se viola una restricción o falla la conexión
     */
    boolean actualizar(T entidad) throws DaoException;

    /**
     * Elimina un registro (sentencia {@code DELETE}).
     *
     * @param id llave primaria del registro a eliminar
     * @return {@code true} si se eliminó una fila
     * @throws DaoException si el registro tiene dependencias o falla la conexión
     */
    boolean eliminar(int id) throws DaoException;
}
