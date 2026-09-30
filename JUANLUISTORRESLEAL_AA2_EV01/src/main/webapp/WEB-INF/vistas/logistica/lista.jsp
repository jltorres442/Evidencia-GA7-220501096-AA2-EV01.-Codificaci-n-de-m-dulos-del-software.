<%--
    VISTA: logistica/lista.jsp
    Consulta del personal de logística (GET /logistica?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Logística"/>
<c:set var="moduloActivo" value="logistica"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Personal de logística</h2>
        <p>Responsables de preparar y despachar los envíos.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/logistica?accion=nuevo">+ Nuevo responsable</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/logistica"/>
    <jsp:param name="ayuda" value="Buscar por nombre, correo, identificación o teléfono"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Identificación</th>
            <th>Correo electrónico</th>
            <th>Teléfono</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="responsable" items="${registros}">
            <tr>
                <td>${responsable.idLogistica}</td>
                <td><strong><c:out value="${responsable.nombre}"/></strong></td>
                <td><c:out value="${responsable.numeroIdentificacion}"/></td>
                <td><c:out value="${responsable.email}"/></td>
                <td><c:out value="${responsable.telefono}"/></td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/logistica"/>
                        <jsp:param name="id" value="${responsable.idLogistica}"/>
                        <jsp:param name="descripcion" value="a ${responsable.nombre}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="6" class="tabla-vacia">No se encontró personal de logística.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
