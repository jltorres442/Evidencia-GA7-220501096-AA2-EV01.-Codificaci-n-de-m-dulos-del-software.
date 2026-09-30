<%--
    VISTA: inicio.jsp
    Panel principal. El personal interno ve indicadores y pedidos recientes;
    el cliente ve el historial de sus pedidos.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Inicio"/>
<c:set var="moduloActivo" value="inicio"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Hola, <c:out value="${usuario.nombre}"/></h2>
        <p>
            <c:choose>
                <c:when test="${usuario.rol.personalInterno}">Resumen de la operación de pedidos y envíos.</c:when>
                <c:otherwise>Aquí puede consultar el estado de sus pedidos.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <c:if test="${usuario.rol.personalInterno}">
        <a class="boton boton--banda" href="${raiz}/pedidos?accion=nuevo">+ Nuevo pedido</a>
    </c:if>
</section>

<c:if test="${not empty indicadores}">
    <section class="indicadores" aria-label="Indicadores">
        <div class="indicador azul"><strong>${indicadores.pedidosActivos}</strong><span>Pedidos en proceso</span></div>
        <div class="indicador verde"><strong>${indicadores.pedidosEntregados}</strong><span>Pedidos entregados</span></div>
        <div class="indicador morado"><strong>${indicadores.enviosEnRuta}</strong><span>Envíos en ruta</span></div>
        <div class="indicador rojo"><strong>${indicadores.incidenciasAbiertas}</strong><span>Incidencias abiertas</span></div>
        <div class="indicador amarillo"><strong>${indicadores.clientes}</strong><span>Clientes registrados</span></div>
    </section>
</c:if>

<section class="tarjeta">
    <h3 class="tarjeta__titulo">
        <c:choose>
            <c:when test="${usuario.rol.personalInterno}">Pedidos recientes</c:when>
            <c:otherwise>Mis pedidos</c:otherwise>
        </c:choose>
    </h3>
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
            </tr>
            </thead>
            <tbody>
            <c:forEach var="pedido" items="${pedidos}">
                <tr>
                    <td><strong>#${pedido.idPedido}</strong></td>
                    <td class="fecha">${pedido.fecha}</td>
                    <td><c:out value="${pedido.nombreCliente}"/></td>
                    <td><c:out value="${pedido.nombreVendedor}"/></td>
                    <td><span class="etiqueta etiqueta--${pedido.estado}">${fn:replace(pedido.estado, '_', ' ')}</span></td>
                    <td class="numero"><fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="$ " maxFractionDigits="0"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty pedidos}">
                <tr><td colspan="6" class="tabla-vacia">No hay pedidos para mostrar.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <c:if test="${usuario.rol.personalInterno}">
        <p><a href="${raiz}/pedidos">Ver todos los pedidos &rarr;</a></p>
    </c:if>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
