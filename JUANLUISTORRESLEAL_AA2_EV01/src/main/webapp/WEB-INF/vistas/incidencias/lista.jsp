<%--
    VISTA: incidencias/lista.jsp
    Consulta de incidencias (GET /incidencias?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Incidencias"/>
<c:set var="moduloActivo" value="incidencias"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Incidencias de entrega</h2>
        <p>Problemas reportados por los transportistas durante la entrega.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/incidencias?accion=nuevo">+ Reportar incidencia</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/incidencias"/>
    <jsp:param name="ayuda" value="Buscar por descripción, estado o número de envío"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>N.º</th>
            <th>Envío</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="incidencia" items="${registros}">
            <tr>
                <td><strong>#${incidencia.idIncidencia}</strong></td>
                <td>
                    Envío #${incidencia.idEnvio}
                    <span class="secundario"><c:out value="${incidencia.direccionEnvio}"/></span>
                </td>
                <td><c:out value="${incidencia.descripcion}"/></td>
                <td><span class="etiqueta etiqueta--${incidencia.estado}">${fn:replace(incidencia.estado, '_', ' ')}</span></td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/incidencias"/>
                        <jsp:param name="id" value="${incidencia.idIncidencia}"/>
                        <jsp:param name="descripcion" value="la incidencia #${incidencia.idIncidencia}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="5" class="tabla-vacia">No se encontraron incidencias.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
