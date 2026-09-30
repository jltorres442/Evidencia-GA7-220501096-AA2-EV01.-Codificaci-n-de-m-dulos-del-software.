package co.homecenter.envios.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fábrica de conexiones JDBC hacia la base de datos {@code homecenter_db}.
 *
 * <p>Lee los parámetros del archivo {@code db.properties} ubicado en el
 * classpath (carpeta {@code src/main/resources}). Cada parámetro puede
 * sobrescribirse con una variable de entorno, lo que permite usar otras
 * credenciales sin recompilar la aplicación:</p>
 *
 * <ul>
 *   <li>{@code HC_DB_URL} &rarr; {@code db.url}</li>
 *   <li>{@code HC_DB_USUARIO} &rarr; {@code db.usuario}</li>
 *   <li>{@code HC_DB_CONTRASENA} &rarr; {@code db.contrasena}</li>
 * </ul>
 *
 * <p>La clase es final y su constructor privado porque solo expone métodos
 * estáticos (patrón <em>utility class</em>).</p>
 */
public final class ConexionBD {

    /** Nombre del archivo de configuración dentro del classpath. */
    private static final String ARCHIVO_PROPIEDADES = "db.properties";

    /** Propiedades cargadas una única vez al inicializar la clase. */
    private static final Properties PROPIEDADES = cargarPropiedades();

    private ConexionBD() {
        // Evita la creación de instancias.
    }

    /**
     * Abre una nueva conexión con la base de datos.
     *
     * <p>Quien invoca este método es responsable de cerrar la conexión,
     * idealmente con una sentencia <em>try-with-resources</em>.</p>
     *
     * @return conexión JDBC abierta
     * @throws SQLException si el servidor no está disponible o las credenciales son incorrectas
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = leerParametro("HC_DB_URL", "db.url");
        String usuario = leerParametro("HC_DB_USUARIO", "db.usuario");
        String contrasena = leerParametro("HC_DB_CONTRASENA", "db.contrasena");
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    /**
     * Carga el archivo de propiedades y registra el controlador JDBC.
     *
     * <p>Registrar el controlador de forma explícita con {@code Class.forName}
     * evita problemas de carga en Tomcat, donde el {@code DriverManager}
     * no siempre detecta controladores empaquetados dentro del WAR.</p>
     *
     * @return propiedades de conexión
     */
    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        ClassLoader cargador = Thread.currentThread().getContextClassLoader();
        try (InputStream entrada = cargador.getResourceAsStream(ARCHIVO_PROPIEDADES)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró " + ARCHIVO_PROPIEDADES + " en el classpath");
            }
            propiedades.load(entrada);
            Class.forName(propiedades.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException excepcion) {
            throw new IllegalStateException("No fue posible inicializar la conexión a la base de datos", excepcion);
        }
        return propiedades;
    }

    /**
     * Devuelve el valor de un parámetro, dando prioridad a la variable de entorno.
     *
     * @param variableEntorno nombre de la variable de entorno
     * @param clavePropiedad  clave dentro de {@code db.properties}
     * @return valor configurado (cadena vacía si no existe)
     */
    private static String leerParametro(String variableEntorno, String clavePropiedad) {
        String valorEntorno = System.getenv(variableEntorno);
        if (valorEntorno != null) {
            return valorEntorno;
        }
        return PROPIEDADES.getProperty(clavePropiedad, "");
    }
}
