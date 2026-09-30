package co.homecenter.envios.util;

import java.util.List;

/**
 * Valores permitidos para las columnas {@code estado} de la base de datos.
 *
 * <p>Deben coincidir con las restricciones {@code CHECK} definidas en
 * {@code database/homecenter_db.sql}. Las listas son inmutables y se publican
 * en el contexto de la aplicación para que las vistas JSP construyan sus
 * listas desplegables.</p>
 */
public final class CatalogoEstados {

    /** Ciclo de vida de un pedido. */
    public static final List<String> ESTADOS_PEDIDO =
            List.of("PENDIENTE", "EN_PREPARACION", "DESPACHADO", "ENTREGADO", "CANCELADO");

    /** Ciclo de vida de un envío. */
    public static final List<String> ESTADOS_ENVIO =
            List.of("PROGRAMADO", "EN_RUTA", "ENTREGADO", "CON_INCIDENCIA");

    /** Seguimiento de una incidencia. */
    public static final List<String> ESTADOS_INCIDENCIA =
            List.of("ABIERTA", "EN_PROCESO", "RESUELTA");

    private CatalogoEstados() {
        // Clase de constantes: no se instancia.
    }
}
