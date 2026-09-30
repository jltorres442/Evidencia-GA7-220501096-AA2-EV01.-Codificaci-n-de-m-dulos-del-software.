<%--
    VISTA: transportistas/lista.jsp
    Consulta de transportistas (GET /transportistas?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Transportistas"/>
<c:set var="moduloActivo" value="transportistas"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Transportistas</h2>
        <p>Conductores que entregan los pedidos a los clientes.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/transportistas?accion=nuevo">+ Nuevo transportista</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/transportistas"/>
    <jsp:param name="ayuda" value="Buscar por nombre, correo, identificación o licencia"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Identificación</th>
            <th>Contacto</th>
            <th>Licencia</th>
            <th>Envío asignado</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="transportista" items="${registros}">
            <tr>
                <td>${transportista.idTransportista}</td>
                <td><strong><c:out value="${transportista.nombre}"/></strong></td>
                <td><c:out value="${transportista.numeroIdentificacion}"/></td>
                <td>
                    <c:out value="${transportista.email}"/>
                    <span class="secundario"><c:out value="${transportista.telefono}"/></span>
                </td>
                <td><c:out value="${transportista.licencia}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty transportista.idEnvio}">
                            <strong>#${transportista.idEnvio}</strong>
                            <span class="secundario"><c:out value="${transportista.direccionEnvio}"/></span>
                        </c:when>
                        <c:otherwise><span class="etiqueta etiqueta--neutra">DISPONIBLE</span></c:otherwise>
                    </c:choose>
                </td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/transportistas"/>
                        <jsp:param name="id" value="${transportista.idTransportista}"/>
                        <jsp:param name="descripcion" value="al transportista ${transportista.nombre}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="7" class="tabla-vacia">No se encontraron transportistas.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
