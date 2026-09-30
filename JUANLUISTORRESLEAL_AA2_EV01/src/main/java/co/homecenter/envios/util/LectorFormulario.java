package co.homecenter.envios.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Lee, convierte y valida los parámetros enviados por un formulario HTML.
 *
 * <p>Cada método toma el nombre del campo, intenta convertir su valor al tipo
 * de Java correspondiente y, si el valor no es válido, registra un mensaje de
 * error asociado a ese campo. Al final el servlet consulta
 * {@link #tieneErrores()} para decidir si guarda los datos o vuelve a mostrar
 * el formulario con los mensajes.</p>
 *
 * <pre>
 * LectorFormulario formulario = new LectorFormulario(solicitud);
 * cliente.setNombre(formulario.texto("nombre", "El nombre", 100, true));
 * cliente.setEmail(formulario.email("email", true));
 * if (formulario.tieneErrores()) { ... }
 * </pre>
 */
public class LectorFormulario {

    /** Correo con usuario, arroba, dominio y extensión. */
    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$");

    /** Teléfono colombiano: 7 a 15 dígitos, opcionalmente con prefijo +. */
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\+?[0-9]{7,15}$");

    /** Documento de identidad: 5 a 20 dígitos o letras (pasaporte). */
    private static final Pattern PATRON_IDENTIFICACION = Pattern.compile("^[0-9A-Za-z]{5,20}$");

    /** Contraseña: mínimo 8 caracteres con al menos una letra y un número. */
    private static final Pattern PATRON_CONTRASENA = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,72}$");

    private final HttpServletRequest solicitud;
    private final Map<String, String> errores = new LinkedHashMap<>();

    /**
     * @param solicitud petición HTTP que contiene los parámetros del formulario
     */
    public LectorFormulario(HttpServletRequest solicitud) {
        this.solicitud = solicitud;
    }

    /**
     * Lee un texto libre, eliminando espacios al inicio y al final.
     *
     * @param campo          nombre del parámetro
     * @param etiqueta       nombre legible del campo para el mensaje de error
     * @param longitudMaxima número máximo de caracteres permitido
     * @param requerido      si el campo es obligatorio
     * @return texto leído o {@code null} si llegó vacío
     */
    public String texto(String campo, String etiqueta, int longitudMaxima, boolean requerido) {
        String valor = leerSinEspacios(campo);
        if (valor == null) {
            if (requerido) {
                registrarError(campo, etiqueta + " es obligatorio.");
            }
            return null;
        }
        if (valor.length() > longitudMaxima) {
            registrarError(campo, etiqueta + " admite máximo " + longitudMaxima + " caracteres.");
        }
        return valor;
    }

    /**
     * Lee un correo electrónico y lo normaliza a minúsculas.
     *
     * @param campo     nombre del parámetro
     * @param requerido si el campo es obligatorio
     * @return correo normalizado o {@code null}
     */
    public String email(String campo, boolean requerido) {
        String valor = texto(campo, "El correo electrónico", 150, requerido);
        if (valor == null) {
            return null;
        }
        valor = valor.toLowerCase();
        if (!PATRON_EMAIL.matcher(valor).matches()) {
            registrarError(campo, "Ingrese un correo electrónico válido (ej. nombre@dominio.com).");
        }
        return valor;
    }

    /**
     * Lee un número de teléfono, descartando espacios y guiones de separación.
     *
     * @param campo     nombre del parámetro
     * @param requerido si el campo es obligatorio
     * @return teléfono solo con dígitos o {@code null}
     */
    public String telefono(String campo, boolean requerido) {
        String valor = texto(campo, "El teléfono", 20, requerido);
        if (valor == null) {
            return null;
        }
        valor = valor.replaceAll("[\\s-]", "");
        if (!PATRON_TELEFONO.matcher(valor).matches()) {
            registrarError(campo, "El teléfono debe tener entre 7 y 15 dígitos.");
        }
        return valor;
    }

    /**
     * Lee un número de identificación (cédula, NIT o pasaporte).
     *
     * @param campo     nombre del parámetro
     * @param requerido si el campo es obligatorio
     * @return identificación sin puntos ni espacios, o {@code null}
     */
    public String identificacion(String campo, boolean requerido) {
        String valor = texto(campo, "El número de identificación", 20, requerido);
        if (valor == null) {
            return null;
        }
        valor = valor.replaceAll("[.\\s]", "");
        if (!PATRON_IDENTIFICACION.matcher(valor).matches()) {
            registrarError(campo, "La identificación debe tener entre 5 y 20 caracteres alfanuméricos.");
        }
        return valor;
    }

    /**
     * Lee una contraseña y verifica que cumpla la política mínima de seguridad.
     *
     * <p>A diferencia de los demás campos, la contraseña no se recorta, porque
     * los espacios pueden ser parte intencional de la clave.</p>
     *
     * @param campo     nombre del parámetro
     * @param requerido {@code true} al crear un registro; {@code false} al
     *                  editar, donde dejarla vacía conserva la contraseña actual
     * @return contraseña en texto plano o {@code null} si llegó vacía
     */
    public String contrasena(String campo, boolean requerido) {
        String valor = solicitud.getParameter(campo);
        if (valor == null || valor.isEmpty()) {
            if (requerido) {
                registrarError(campo, "La contraseña es obligatoria.");
            }
            return null;
        }
        if (!PATRON_CONTRASENA.matcher(valor).matches()) {
            registrarError(campo, "La contraseña debe tener mínimo 8 caracteres e incluir letras y números.");
        }
        return valor;
    }

    /**
     * Lee un número entero, típicamente el identificador de una llave foránea.
     *
     * @param campo     nombre del parámetro
     * @param etiqueta  nombre legible del campo
     * @param requerido si el campo es obligatorio
     * @return número leído o {@code null}
     */
    public Integer entero(String campo, String etiqueta, boolean requerido) {
        String valor = leerSinEspacios(campo);
        if (valor == null) {
            if (requerido) {
                registrarError(campo, "Seleccione " + etiqueta.toLowerCase() + ".");
            }
            return null;
        }
        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException formatoInvalido) {
            registrarError(campo, etiqueta + " no es un número válido.");
            return null;
        }
    }

    /**
     * Lee un valor monetario no negativo con máximo dos decimales.
     *
     * @param campo     nombre del parámetro
     * @param etiqueta  nombre legible del campo
     * @param requerido si el campo es obligatorio
     * @return valor decimal o {@code null}
     */
    public BigDecimal decimal(String campo, String etiqueta, boolean requerido) {
        String valor = leerSinEspacios(campo);
        if (valor == null) {
            if (requerido) {
                registrarError(campo, etiqueta + " es obligatorio.");
            }
            return null;
        }
        try {
            BigDecimal numero = new BigDecimal(valor.replace(",", "."));
            if (numero.signum() < 0) {
                registrarError(campo, etiqueta + " no puede ser negativo.");
            } else if (numero.scale() > 2 || numero.precision() - numero.scale() > 10) {
                registrarError(campo, etiqueta + " admite hasta 10 enteros y 2 decimales.");
            }
            return numero;
        } catch (NumberFormatException formatoInvalido) {
            registrarError(campo, etiqueta + " debe ser un número (ej. 125000.50).");
            return null;
        }
    }

    /**
     * Lee una fecha enviada por un {@code <input type="date">} (formato ISO yyyy-MM-dd).
     *
     * @param campo     nombre del parámetro
     * @param etiqueta  nombre legible del campo
     * @param requerido si el campo es obligatorio
     * @return fecha o {@code null}
     */
    public LocalDate fecha(String campo, String etiqueta, boolean requerido) {
        String valor = leerSinEspacios(campo);
        if (valor == null) {
            if (requerido) {
                registrarError(campo, etiqueta + " es obligatoria.");
            }
            return null;
        }
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException formatoInvalido) {
            registrarError(campo, etiqueta + " no tiene un formato válido.");
            return null;
        }
    }

    /**
     * Lee una fecha y hora enviada por un {@code <input type="datetime-local">}.
     *
     * @param campo     nombre del parámetro
     * @param etiqueta  nombre legible del campo
     * @param requerido si el campo es obligatorio
     * @return fecha y hora o {@code null}
     */
    public LocalDateTime fechaHora(String campo, String etiqueta, boolean requerido) {
        String valor = leerSinEspacios(campo);
        if (valor == null) {
            if (requerido) {
                registrarError(campo, etiqueta + " es obligatoria.");
            }
            return null;
        }
        try {
            return LocalDateTime.parse(valor);
        } catch (DateTimeParseException formatoInvalido) {
            registrarError(campo, etiqueta + " no tiene un formato válido.");
            return null;
        }
    }

    /**
     * Lee un valor que debe pertenecer a una lista cerrada (por ejemplo, un estado).
     *
     * @param campo           nombre del parámetro
     * @param etiqueta        nombre legible del campo
     * @param valoresValidos  opciones permitidas
     * @return valor leído o {@code null}
     */
    public String opcion(String campo, String etiqueta, Collection<String> valoresValidos) {
        String valor = leerSinEspacios(campo);
        if (valor == null || !valoresValidos.contains(valor)) {
            registrarError(campo, "Seleccione un valor válido para " + etiqueta.toLowerCase() + ".");
            return null;
        }
        return valor;
    }

    /**
     * Indica si una casilla de verificación ({@code checkbox}) fue marcada.
     *
     * @param campo nombre del parámetro
     * @return {@code true} si el navegador envió el parámetro
     */
    public boolean casilla(String campo) {
        return solicitud.getParameter(campo) != null;
    }

    /**
     * Registra un error de validación adicional (por ejemplo, contraseñas que no coinciden).
     * Si el campo ya tiene un error, se conserva el primero.
     *
     * @param campo   nombre del campo
     * @param mensaje texto que se mostrará bajo el campo
     */
    public void registrarError(String campo, String mensaje) {
        errores.putIfAbsent(campo, mensaje);
    }

    /** @return {@code true} si al menos un campo es inválido */
    public boolean tieneErrores() {
        return !errores.isEmpty();
    }

    /** @return mapa campo &rarr; mensaje, en el orden en que se detectaron */
    public Map<String, String> getErrores() {
        return errores;
    }

    /**
     * Obtiene un parámetro sin espacios en los extremos.
     *
     * @param campo nombre del parámetro
     * @return valor o {@code null} si no llegó o está vacío
     */
    private String leerSinEspacios(String campo) {
        String valor = solicitud.getParameter(campo);
        if (valor == null) {
            return null;
        }
        valor = valor.trim();
        return valor.isEmpty() ? null : valor;
    }
}
