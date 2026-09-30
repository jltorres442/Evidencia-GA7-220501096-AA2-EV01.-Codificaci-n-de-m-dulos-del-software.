package co.homecenter.envios.modelo;

/**
 * Representa un registro de la tabla {@code Transportista}: el conductor
 * que entrega los pedidos en la dirección del cliente.
 */
public class Transportista extends Usuario {

    private static final long serialVersionUID = 1L;

    private Integer idTransportista;

    /** Número de licencia de conducción. */
    private String licencia;

    /** Envío asignado actualmente; {@code null} si está disponible. */
    private Integer idEnvio;

    /** Dirección del envío asignado (dato de consulta, no se guarda). */
    private String direccionEnvio;

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
