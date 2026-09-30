package co.homecenter.envios.modelo;

import java.io.Serializable;

/**
 * Datos mínimos del usuario autenticado que se guardan en la sesión HTTP.
 *
 * <p>Deliberadamente no incluye la contraseña ni su hash: la sesión solo
 * necesita saber quién es el usuario y qué rol tiene. Es inmutable porque
 * sus datos no cambian mientras la sesión esté activa.</p>
 */
public final class UsuarioSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String nombre;
    private final String email;
    private final Rol rol;

    /**
     * @param id     llave primaria en la tabla del rol
     * @param nombre nombre completo
     * @param email  correo electrónico
     * @param rol    rol del usuario
     */
    public UsuarioSesion(int id, String nombre, String email, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {
        return rol;
    }

    /**
     * Iniciales del nombre para el avatar de la barra superior.
     *
     * @return una o dos letras en mayúscula
     */
    public String getIniciales() {
        String[] palabras = nombre.trim().split("\\s+");
        StringBuilder iniciales = new StringBuilder();
        for (int indice = 0; indice < palabras.length && iniciales.length() < 2; indice++) {
            if (!palabras[indice].isEmpty()) {
                iniciales.append(Character.toUpperCase(palabras[indice].charAt(0)));
            }
        }
        return iniciales.toString();
    }
}
