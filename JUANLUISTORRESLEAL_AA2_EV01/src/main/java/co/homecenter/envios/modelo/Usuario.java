package co.homecenter.envios.modelo;

import java.io.Serializable;

/**
 * Datos comunes de los actores que pueden iniciar sesión en el sistema:
 * {@link Cliente}, {@link Vendedor}, {@link Logistica} y {@link Transportista}.
 *
 * <p>Es una clase abstracta porque en la base de datos no existe una tabla
 * "Usuario"; cada actor tiene su propia tabla con estas mismas columnas.
 * Heredar de esta clase evita repetir atributos, getters y setters.</p>
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Cédula, NIT o pasaporte. Único por tabla. */
    private String numeroIdentificacion;

    /** Nombre completo. */
    private String nombre;

    /** Correo electrónico, usado como nombre de usuario al iniciar sesión. */
    private String email;

    /**
     * Hash PBKDF2 de la contraseña, tal como está en la base de datos.
     * Nunca contiene la contraseña en texto plano.
     */
    private String contrasena;

    /** Número de teléfono o celular de contacto. */
    private String telefono;

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
