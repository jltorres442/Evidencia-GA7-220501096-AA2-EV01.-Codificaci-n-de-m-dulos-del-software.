<%--
    VISTA: login.jsp
    Formulario de inicio de sesión (envía POST a LoginServlet).
    Basado en la pantalla 1.1.1 de la evidencia GA6-AA3-EV02.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión · Homecenter Envíos</title>
    <link rel="stylesheet" href="${raiz}/css/estilos.css">
</head>
<body>
<main class="pagina-acceso">
    <div class="tarjeta-acceso">
        <div class="logo" aria-hidden="true">HC</div>
        <h1>Iniciar Sesión</h1>
        <p class="subtitulo">Sistema de realización y seguimiento de pedidos</p>

        <jsp:include page="/WEB-INF/jspf/mensajes.jsp"/>

        <form method="post" action="${raiz}/login" novalidate>
            <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">

            <div class="campo">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" required autofocus autocomplete="username"
                       value="${fn:escapeXml(email)}" placeholder="nombre@homecenter.com">
            </div>

            <div class="campo">
                <label for="contrasena">Contraseña</label>
                <div class="contrasena-envoltura">
                    <input type="password" id="contrasena" name="contrasena" required
                           autocomplete="current-password" placeholder="••••••••">
                    <button type="button" class="mostrar-contrasena" data-campo="contrasena">MOSTRAR</button>
                </div>
            </div>

            <button type="submit" class="boton boton--primario boton--bloque">INGRESAR</button>
        </form>

        <div class="enlaces-acceso">
            ¿No tiene cuenta? <a href="${raiz}/registro">Regístrese aquí</a>
        </div>

        <p class="nota-acceso">
            Portal para <strong>Clientes</strong>, <strong>Vendedores</strong>,
            <strong>Logística</strong> y <strong>Transportistas</strong>.
        </p>
    </div>
</main>
<script src="${raiz}/js/aplicacion.js"></script>
</body>
</html>
