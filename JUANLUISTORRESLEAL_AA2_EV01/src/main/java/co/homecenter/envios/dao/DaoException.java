package co.homecenter.envios.dao;

/**
 * Error ocurrido al acceder a la base de datos.
 *
 * <p>El mensaje de esta excepción está redactado para el usuario final
 * (por ejemplo, "Ya existe un registro con ese correo electrónico"), de modo
 * que los servlets pueden mostrarlo directamente. El detalle técnico se
 * conserva en la causa ({@link #getCause()}) para el registro de errores.</p>
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensajeUsuario texto comprensible para el usuario
     * @param causa          excepción SQL original
     */
    public DaoException(String mensajeUsuario, Throwable causa) {
        super(mensajeUsuario, causa);
    }
}
