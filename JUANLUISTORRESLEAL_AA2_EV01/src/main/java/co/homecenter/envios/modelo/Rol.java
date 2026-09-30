package co.homecenter.envios.modelo;

/**
 * Roles de los usuarios que pueden iniciar sesión. Cada rol corresponde a
 * una tabla de la base de datos.
 */
public enum Rol {

    CLIENTE("Cliente"),
    VENDEDOR("Vendedor"),
    LOGISTICA("Logística"),
    TRANSPORTISTA("Transportista");

    /** Nombre legible para mostrar en la interfaz. */
    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Indica si el rol pertenece al personal interno de Homecenter, que
     * tiene acceso a los módulos de administración.
     *
     * @return {@code true} para todos los roles excepto {@link #CLIENTE}
     */
    public boolean isPersonalInterno() {
        return this != CLIENTE;
    }
}
