package co.homecenter.envios.dao;

import co.homecenter.envios.configuracion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Funcionalidad JDBC compartida por todos los DAO.
 *
 * <p>Centraliza la apertura y cierre de recursos ({@link Connection},
 * {@link PreparedStatement} y {@link ResultSet}) con
 * <em>try-with-resources</em>, y traduce los códigos de error de MySQL a
 * mensajes comprensibles para el usuario. Todas las consultas usan
 * parámetros ({@code ?}) para prevenir inyección SQL.</p>
 */
public abstract class BaseDao {

    private static final Logger REGISTRO = Logger.getLogger(BaseDao.class.getName());

    /** Código MySQL: valor duplicado en una llave única. */
    private static final int ERROR_DUPLICADO = 1062;
    /** Código MySQL: no se puede borrar una fila padre con hijos. */
    private static final int ERROR_FILA_REFERENCIADA = 1451;
    /** Código MySQL: la llave foránea apunta a una fila inexistente. */
    private static final int ERROR_REFERENCIA_INEXISTENTE = 1452;
    /** Códigos MySQL (3819) y MariaDB (4025): se violó una restricción CHECK. */
    private static final int ERROR_CHECK_MYSQL = 3819;
    private static final int ERROR_CHECK_MARIADB = 4025;

    /**
     * Asigna los valores de los parámetros {@code ?} de una sentencia.
     */
    @FunctionalInterface
    protected interface AsignadorParametros {
        void asignar(PreparedStatement sentencia) throws SQLException;
    }

    /**
     * Convierte la fila actual de un {@link ResultSet} en un objeto del modelo.
     *
     * @param <T> tipo del objeto resultante
     */
    @FunctionalInterface
    protected interface MapeadorFila<T> {
        T mapear(ResultSet resultado) throws SQLException;
    }

    /** Asignador vacío para consultas sin parámetros. */
    protected static final AsignadorParametros SIN_PARAMETROS = sentencia -> { };

    /**
     * Ejecuta un {@code SELECT} y convierte cada fila en un objeto.
     *
     * @param sql        consulta con parámetros {@code ?}
     * @param asignador  asigna los valores de los parámetros
     * @param mapeador   convierte cada fila
     * @param <T>        tipo de los objetos
     * @return lista con los resultados
     * @throws DaoException si ocurre un error SQL
     */
    protected <T> List<T> consultarLista(String sql, AsignadorParametros asignador,
                                         MapeadorFila<T> mapeador) throws DaoException {
        List<T> resultados = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignador.asignar(sentencia);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    resultados.add(mapeador.mapear(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw traducirError(excepcion, "consultar la información");
        }
        return resultados;
    }

    /**
     * Ejecuta un {@code SELECT} que devuelve como máximo una fila.
     *
     * @param sql       consulta con parámetros {@code ?}
     * @param asignador asigna los valores de los parámetros
     * @param mapeador  convierte la fila
     * @param <T>       tipo del objeto
     * @return el objeto encontrado o vacío
     * @throws DaoException si ocurre un error SQL
     */
    protected <T> Optional<T> consultarUno(String sql, AsignadorParametros asignador,
                                           MapeadorFila<T> mapeador) throws DaoException {
        List<T> resultados = consultarLista(sql, asignador, mapeador);
        return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
    }

    /**
     * Ejecuta un {@code SELECT COUNT(*)} u otra consulta que devuelve un único número.
     *
     * @param sql consulta sin parámetros
     * @return valor de la primera columna de la primera fila
     * @throws DaoException si ocurre un error SQL
     */
    protected long consultarNumero(String sql) throws DaoException {
        return consultarUno(sql, SIN_PARAMETROS, resultado -> resultado.getLong(1)).orElse(0L);
    }

    /**
     * Ejecuta un {@code INSERT} y devuelve la llave primaria generada por
     * {@code AUTO_INCREMENT}.
     *
     * @param sql       sentencia con parámetros {@code ?}
     * @param asignador asigna los valores de los parámetros
     * @return identificador generado
     * @throws DaoException si ocurre un error SQL
     */
    protected int ejecutarInsercion(String sql, AsignadorParametros asignador) throws DaoException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignador.asignar(sentencia);
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                return llaves.next() ? llaves.getInt(1) : 0;
            }
        } catch (SQLException excepcion) {
            throw traducirError(excepcion, "guardar el registro");
        }
    }

    /**
     * Ejecuta un {@code UPDATE} o {@code DELETE}.
     *
     * @param sql       sentencia con parámetros {@code ?}
     * @param asignador asigna los valores de los parámetros
     * @return número de filas afectadas
     * @throws DaoException si ocurre un error SQL
     */
    protected int ejecutarModificacion(String sql, AsignadorParametros asignador) throws DaoException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignador.asignar(sentencia);
            return sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw traducirError(excepcion, "modificar el registro");
        }
    }

    /**
     * Construye el patrón para una búsqueda con {@code LIKE}.
     *
     * @param filtro texto digitado por el usuario (puede ser {@code null})
     * @return {@code "%texto%"}, o {@code "%"} si no hay filtro
     */
    protected static String patronBusqueda(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return "%";
        }
        // Se escapan los comodines para que el usuario busque literalmente "%" o "_".
        String textoEscapado = filtro.trim()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + textoEscapado + "%";
    }

    /**
     * Asigna un entero que puede ser {@code null} (llave foránea opcional).
     *
     * @param sentencia sentencia preparada
     * @param posicion  posición del parámetro (inicia en 1)
     * @param valor     valor o {@code null}
     * @throws SQLException si la posición no es válida
     */
    protected static void asignarEnteroOpcional(PreparedStatement sentencia, int posicion,
                                                Integer valor) throws SQLException {
        if (valor == null) {
            sentencia.setNull(posicion, Types.INTEGER);
        } else {
            sentencia.setInt(posicion, valor);
        }
    }

    /**
     * Lee una columna entera que puede contener {@code NULL}.
     *
     * @param resultado fila actual
     * @param columna   nombre de la columna
     * @return valor o {@code null}
     * @throws SQLException si la columna no existe
     */
    protected static Integer leerEnteroOpcional(ResultSet resultado, String columna) throws SQLException {
        int valor = resultado.getInt(columna);
        return resultado.wasNull() ? null : valor;
    }

    /**
     * Convierte una {@link SQLException} en una {@link DaoException} con un
     * mensaje entendible para el usuario, y registra el detalle técnico.
     *
     * @param excepcion error original
     * @param accion    descripción de lo que se intentaba hacer
     * @return excepción lista para lanzar
     */
    protected static DaoException traducirError(SQLException excepcion, String accion) {
        String mensajeTecnico = String.valueOf(excepcion.getMessage()).toLowerCase();
        String mensajeUsuario;

        switch (excepcion.getErrorCode()) {
            case ERROR_DUPLICADO:
                if (mensajeTecnico.contains("email")) {
                    mensajeUsuario = "Ya existe un registro con ese correo electrónico.";
                } else if (mensajeTecnico.contains("identificacion")) {
                    mensajeUsuario = "Ya existe un registro con ese número de identificación.";
                } else if (mensajeTecnico.contains("pedido")) {
                    mensajeUsuario = "El pedido seleccionado ya tiene un registro asociado.";
                } else {
                    mensajeUsuario = "El registro ya existe.";
                }
                break;
            case ERROR_FILA_REFERENCIADA:
                mensajeUsuario = "No se puede eliminar: hay otros registros que dependen de este.";
                break;
            case ERROR_REFERENCIA_INEXISTENTE:
                mensajeUsuario = "El registro relacionado que seleccionó ya no existe.";
                break;
            case ERROR_CHECK_MYSQL:
            case ERROR_CHECK_MARIADB:
                mensajeUsuario = "Alguno de los valores no cumple las reglas de la base de datos.";
                break;
            default:
                REGISTRO.log(Level.SEVERE, "Error SQL al " + accion, excepcion);
                mensajeUsuario = "No fue posible " + accion
                        + ". Verifique la conexión con la base de datos e intente de nuevo.";
                break;
        }
        return new DaoException(mensajeUsuario, excepcion);
    }
}
