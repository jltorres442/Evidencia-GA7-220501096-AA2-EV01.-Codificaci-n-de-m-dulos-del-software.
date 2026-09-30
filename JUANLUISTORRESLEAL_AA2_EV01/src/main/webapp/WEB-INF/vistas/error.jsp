<%--
    VISTA: error.jsp
    Página de error configurada en web.xml. La directiva isErrorPage="true"
    habilita el objeto implícito "exception" y los atributos estándar
    javax.servlet.error.*, de los cuales se lee el código HTTP.
    No muestra detalles técnicos al usuario (se registran en el log de Tomcat).
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="codigo" value="${requestScope['javax.servlet.error.status_code']}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error · Homecenter Envíos</title>
    <link rel="stylesheet" href="${raiz}/css/estilos.css">
</head>
<body>
<main class="pagina-acceso">
    <div class="tarjeta-acceso pagina-error">
        <p class="codigo">${empty codigo ? 500 : codigo}</p>
        <h1>
            <c:choose>
                <c:when test="${codigo == 403}">Acceso restringido</c:when>
                <c:when test="${codigo == 404}">Página no encontrada</c:when>
                <c:otherwise>Ocurrió un error inesperado</c:otherwise>
            </c:choose>
        </h1>
        <p class="subtitulo">
            <c:choose>
                <c:when test="${codigo == 403}">Su rol no tiene permiso para ver esta sección.</c:when>
                <c:when test="${codigo == 404}">La dirección que buscó no existe o fue movida.</c:when>
                <c:otherwise>Intente de nuevo en unos minutos. Si el problema continúa, contacte al administrador.</c:otherwise>
            </c:choose>
        </p>
        <a class="boton boton--primario" href="${raiz}/inicio">Volver al inicio</a>
    </div>
</main>
</body>
</html>
