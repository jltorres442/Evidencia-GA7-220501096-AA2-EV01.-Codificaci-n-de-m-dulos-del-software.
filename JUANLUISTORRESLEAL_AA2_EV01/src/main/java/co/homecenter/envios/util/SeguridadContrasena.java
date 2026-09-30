package co.homecenter.envios.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidades para proteger contraseñas antes de guardarlas en la base de datos.
 *
 * <p>Se usa el algoritmo <strong>PBKDF2 con HMAC-SHA256</strong>, incluido en
 * el JDK, con una sal aleatoria de 16 bytes por contraseña. El valor almacenado
 * tiene el formato:</p>
 *
 * <pre>pbkdf2$&lt;iteraciones&gt;$&lt;sal en Base64&gt;$&lt;hash en Base64&gt;</pre>
 *
 * <p>Guardar las iteraciones y la sal junto al hash permite verificar la
 * contraseña más adelante aunque se cambien los parámetros por defecto.</p>
 */
public final class SeguridadContrasena {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIJO = "pbkdf2";
    private static final int ITERACIONES = 65_536;
    private static final int LONGITUD_SAL_BYTES = 16;
    private static final int LONGITUD_HASH_BITS = 256;
    private static final SecureRandom GENERADOR_ALEATORIO = new SecureRandom();

    private SeguridadContrasena() {
        // Clase de utilidades: no se instancia.
    }

    /**
     * Genera el hash seguro de una contraseña en texto plano.
     *
     * @param contrasenaPlana contraseña digitada por el usuario
     * @return cadena lista para almacenar en la columna {@code contrasena}
     */
    public static String generarHash(String contrasenaPlana) {
        byte[] sal = new byte[LONGITUD_SAL_BYTES];
        GENERADOR_ALEATORIO.nextBytes(sal);
        byte[] hash = calcularPbkdf2(contrasenaPlana.toCharArray(), sal, ITERACIONES);
        Base64.Encoder codificador = Base64.getEncoder();
        return String.join("$", PREFIJO, String.valueOf(ITERACIONES),
                codificador.encodeToString(sal), codificador.encodeToString(hash));
    }

    /**
     * Comprueba si una contraseña en texto plano corresponde al hash almacenado.
     *
     * <p>La comparación usa {@link MessageDigest#isEqual(byte[], byte[])}, que
     * tarda lo mismo sin importar dónde difieran los bytes, para no filtrar
     * información por tiempos de respuesta.</p>
     *
     * @param contrasenaPlana contraseña digitada en el formulario de ingreso
     * @param hashAlmacenado  valor leído de la base de datos
     * @return {@code true} si la contraseña es correcta
     */
    public static boolean verificar(String contrasenaPlana, String hashAlmacenado) {
        if (contrasenaPlana == null || hashAlmacenado == null) {
            return false;
        }
        String[] partes = hashAlmacenado.split("\\$");
        if (partes.length != 4 || !PREFIJO.equals(partes[0])) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[1]);
            Base64.Decoder decodificador = Base64.getDecoder();
            byte[] sal = decodificador.decode(partes[2]);
            byte[] hashEsperado = decodificador.decode(partes[3]);
            byte[] hashCalculado = calcularPbkdf2(contrasenaPlana.toCharArray(), sal, iteraciones);
            return MessageDigest.isEqual(hashEsperado, hashCalculado);
        } catch (IllegalArgumentException formatoInvalido) {
            return false;
        }
    }

    /**
     * Ejecuta el algoritmo PBKDF2 sobre la contraseña.
     *
     * @param contrasena  caracteres de la contraseña
     * @param sal         sal aleatoria
     * @param iteraciones número de iteraciones del algoritmo
     * @return bytes del hash resultante
     */
    private static byte[] calcularPbkdf2(char[] contrasena, byte[] sal, int iteraciones) {
        PBEKeySpec especificacion = new PBEKeySpec(contrasena, sal, iteraciones, LONGITUD_HASH_BITS);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITMO);
            return fabrica.generateSecret(especificacion).getEncoded();
        } catch (GeneralSecurityException excepcion) {
            throw new IllegalStateException("El algoritmo " + ALGORITMO + " no está disponible", excepcion);
        } finally {
            especificacion.clearPassword();
        }
    }

    /**
     * Punto de entrada auxiliar para generar hashes desde la consola,
     * útil al preparar datos de prueba en el script SQL.
     *
     * <pre>java co.homecenter.envios.util.SeguridadContrasena "MiClave123*"</pre>
     *
     * @param argumentos contraseña a convertir
     */
    public static void main(String[] argumentos) {
        if (argumentos.length != 1) {
            System.err.println("Uso: SeguridadContrasena <contraseña>");
            return;
        }
        System.out.println(generarHash(argumentos[0]));
    }
}
