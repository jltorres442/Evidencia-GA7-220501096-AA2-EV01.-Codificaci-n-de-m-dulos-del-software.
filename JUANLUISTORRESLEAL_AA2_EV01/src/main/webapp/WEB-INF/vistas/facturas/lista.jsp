<%--
    VISTA: facturas/lista.jsp
    Consulta de facturas (GET /facturas?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Facturas"/>
<c:set var="moduloActivo" value="facturas"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Facturas</h2>
        <p>Cada pedido puede tener una única factura.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/facturas?accion=nuevo">+ Nueva factura</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/facturas"/>
    <jsp:param name="ayuda" value="Buscar por número de factura, pedido o cliente"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>N.º factura</th>
            <th>Fecha de emisión</th>
            <th>Pedido</th>
            <th>Cliente</th>
            <th class="numero">Monto total</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="factura" items="${registros}">
            <tr>
                <td><strong>FAC-${factura.idFactura}</strong></td>
                <td class="fecha">${factura.fechaEmision}</td>
                <td>#${factura.idPedido}</td>
                <td><c:out value="${factura.nombreCliente}"/></td>
                <td class="numero">
                    <fmt:formatNumber value="${factura.montoTotal}" type="currency" currencySymbol="$ " maxFractionDigits="0"/>
                </td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/facturas"/>
                        <jsp:param name="id" value="${factura.idFactura}"/>
                        <jsp:param name="descripcion" value="la factura FAC-${factura.idFactura}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="6" class="tabla-vacia">No se encontraron facturas.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
