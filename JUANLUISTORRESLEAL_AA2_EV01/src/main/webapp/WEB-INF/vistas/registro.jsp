<%--
    VISTA: registro.jsp
    Formulario público "Crear cuenta" para nuevos clientes (POST a RegistroServlet).

    Demuestra la acción estándar <jsp:useBean>: si el servlet ya dejó un
    objeto "cliente" en la petición (al devolver el formulario con errores),
    se reutiliza; si no existe (primera visita), se crea uno vacío. Así la
    vista siempre tiene un bean con el cual llenar los campos.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="cliente" class="co.homecenter.envios.modelo.Cliente" scope="request"/>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="registro" value="${cliente}"/>
<c:set var="esNuevo" value="${true}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear cuenta · Homecenter Envíos</title>
    <link rel="stylesheet" href="${raiz}/css/estilos.css">
</head>
<body>
<main class="pagina-acceso">
    <div class="tarjeta-acceso tarjeta-acceso--ancha">
        <div class="logo" aria-hidden="true">HC</div>
        <h1>Crear cuenta</h1>
        <p class="subtitulo">Regístrese para hacer seguimiento a sus pedidos</p>

        <jsp:include page="/WEB-INF/jspf/mensajes.jsp"/>

        <form method="post" action="${raiz}/registro">
            <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">

            <div class="formulario-rejilla">
                <%@ include file="/WEB-INF/jspf/campos-usuario.jspf" %>

                <div class="campo campo--completo ${not empty errores.direccion ? 'campo--error' : ''}">
                    <label for="direccion">Dirección de entrega <span class="obligatorio">*</span></label>
                    <input type="text" id="direccion" name="direccion" maxlength="200" required
                           autocomplete="street-address"
                           value="${fn:escapeXml(registro.direccion)}" placeholder="Ej. Cra. 15 # 93-47, Bogotá">
                    <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                        <jsp:param name="campo" value="direccion"/>
                    </jsp:include>
                </div>

                <div class="campo campo--completo ${not empty errores.aceptaTerminos ? 'campo--error' : ''}">
                    <label class="casilla">
                        <input type="checkbox" name="aceptaTerminos" ${not empty param.aceptaTerminos ? 'checked' : ''}>
                        <span>Autorizo el tratamiento de mis datos personales conforme a la Ley 1581 de 2012.</span>
                    </label>
                    <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                        <jsp:param name="campo" value="aceptaTerminos"/>
                    </jsp:include>
                </div>
            </div>

            <button type="submit" class="boton boton--primario boton--bloque">CREAR CUENTA</button>
        </form>

        <div class="enlaces-acceso">
            ¿Ya tiene cuenta? <a href="${raiz}/login">Inicie sesión</a>
        </div>
    </div>
</main>
<script src="${raiz}/js/aplicacion.js"></script>
</body>
</html>
