package co.homecenter.envios.modelo;

/**
 * Representa un registro de la tabla {@code Logistica}: el personal de
 * bodega que prepara y despacha los envíos.
 */
public class Logistica extends Usuario {

    private static final long serialVersionUID = 1L;

    private Integer idLogistica;

    public Integer getIdLogistica() {
        return idLogistica;
    }

    public void setIdLogistica(Integer idLogistica) {
        this.idLogistica = idLogistica;
    }
}
