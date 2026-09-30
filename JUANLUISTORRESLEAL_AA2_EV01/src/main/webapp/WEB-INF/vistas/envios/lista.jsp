<%--
    VISTA: envios/lista.jsp
    Consulta de envíos (GET /envios?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Envíos"/>
<c:set var="moduloActivo" value="envios"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Envíos de pedidos</h2>
        <p>Despachos gestionados por el personal de logística.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/envios?accion=nuevo">+ Nuevo envío</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/envios"/>
    <jsp:param name="ayuda" value="Buscar por número, dirección, estado o cliente"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>N.º</th>
            <th>Fecha de envío</th>
            <th>Pedido / Cliente</th>
            <th>Dirección de entrega</th>
            <th>Logística</th>
            <th>Estado</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="envio" items="${registros}">
            <tr>
                <td><strong>#${envio.idEnvio}</strong></td>
                <td class="fecha">${envio.fechaEnvio}</td>
                <td>
                    Pedido #${envio.idPedido}
                    <span class="secundario"><c:out value="${envio.nombreCliente}"/></span>
                </td>
                <td><c:out value="${envio.direccionEntrega}"/></td>
                <td><c:out value="${envio.nombreLogistica}"/></td>
                <td><span class="etiqueta etiqueta--${envio.estado}">${fn:replace(envio.estado, '_', ' ')}</span></td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/envios"/>
                        <jsp:param name="id" value="${envio.idEnvio}"/>
                        <jsp:param name="descripcion" value="el envío #${envio.idEnvio} (también se eliminarán sus incidencias)"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="7" class="tabla-vacia">No se encontraron envíos.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
