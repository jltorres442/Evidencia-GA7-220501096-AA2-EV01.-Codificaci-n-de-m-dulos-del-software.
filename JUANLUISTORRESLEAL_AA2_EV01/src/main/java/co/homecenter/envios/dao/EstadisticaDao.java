package co.homecenter.envios.dao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Consultas de resumen para el panel de inicio.
 */
public class EstadisticaDao extends BaseDao {

    /**
     * Cuenta los registros más relevantes del sistema.
     *
     * @return mapa ordenado indicador &rarr; cantidad, listo para iterarse en la vista
     * @throws DaoException si falla alguna consulta
     */
    public Map<String, Long> contarIndicadores() throws DaoException {
        Map<String, Long> indicadores = new LinkedHashMap<>();
        indicadores.put("pedidosActivos", consultarNumero(
                "SELECT COUNT(*) FROM Pedido WHERE estado IN ('PENDIENTE', 'EN_PREPARACION', 'DESPACHADO')"));
        indicadores.put("pedidosEntregados", consultarNumero(
                "SELECT COUNT(*) FROM Pedido WHERE estado = 'ENTREGADO'"));
        indicadores.put("enviosEnRuta", consultarNumero(
                "SELECT COUNT(*) FROM Envio_Pedido WHERE estado = 'EN_RUTA'"));
        indicadores.put("incidenciasAbiertas", consultarNumero(
                "SELECT COUNT(*) FROM Incidencia WHERE estado <> 'RESUELTA'"));
        indicadores.put("clientes", consultarNumero("SELECT COUNT(*) FROM Cliente"));
        return indicadores;
    }
}
