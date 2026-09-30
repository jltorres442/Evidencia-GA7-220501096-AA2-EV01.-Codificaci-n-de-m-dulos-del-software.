<%--
    VISTA: notificaciones/lista.jsp
    Consulta de notificaciones (GET /notificaciones?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Notificaciones"/>
<c:set var="moduloActivo" value="notificaciones"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Notificaciones a transportistas</h2>
        <p>Mensajes enviados a los conductores sobre sus rutas y entregas.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/notificaciones?accion=nuevo">+ Nueva notificación</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/notificaciones"/>
    <jsp:param name="ayuda" value="Buscar por mensaje o transportista"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>N.º</th>
            <th>Fecha y hora</th>
            <th>Transportista</th>
            <th>Mensaje</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="notificacion" items="${registros}">
            <tr>
                <td><strong>#${notificacion.idNotificacion}</strong></td>
                <td class="fecha">${fn:replace(notificacion.fechaHora, 'T', ' ')}</td>
                <td><c:out value="${notificacion.nombreTransportista}"/></td>
                <td><c:out value="${notificacion.mensaje}"/></td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/notificaciones"/>
                        <jsp:param name="id" value="${notificacion.idNotificacion}"/>
                        <jsp:param name="descripcion" value="la notificación #${notificacion.idNotificacion}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="5" class="tabla-vacia">No se encontraron notificaciones.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
