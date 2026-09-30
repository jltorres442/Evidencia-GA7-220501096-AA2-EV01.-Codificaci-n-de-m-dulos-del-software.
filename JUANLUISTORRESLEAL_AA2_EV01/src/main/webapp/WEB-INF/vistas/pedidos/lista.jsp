<%--
    VISTA: pedidos/lista.jsp
    Consulta de pedidos (GET /pedidos?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Pedidos"/>
<c:set var="moduloActivo" value="pedidos"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Pedidos</h2>
        <p>Pedidos registrados por los vendedores para cada cliente.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/pedidos?accion=nuevo">+ Nuevo pedido</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/pedidos"/>
    <jsp:param name="ayuda" value="Buscar por número, estado, cliente o vendedor"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>N.º</th>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Vendedor</th>
            <th>Estado</th>
            <th class="numero">Total</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="pedido" items="${registros}">
            <tr>
                <td><strong>#${pedido.idPedido}</strong></td>
                <td class="fecha">${pedido.fecha}</td>
                <td><c:out value="${pedido.nombreCliente}"/></td>
                <td><c:out value="${pedido.nombreVendedor}"/></td>
                <td><span class="etiqueta etiqueta--${pedido.estado}">${fn:replace(pedido.estado, '_', ' ')}</span></td>
                <td class="numero">
                    <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="$ " maxFractionDigits="0"/>
                </td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/pedidos"/>
                        <jsp:param name="id" value="${pedido.idPedido}"/>
                        <jsp:param name="descripcion" value="el pedido #${pedido.idPedido} (también se eliminarán su factura, envío e incidencias)"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="7" class="tabla-vacia">No se encontraron pedidos.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
