<%--
    VISTA: facturas/formulario.jsp
    Creación y edición de una factura. Recibe "registro", "esNuevo", "errores"
    y el catálogo "pedidos".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nueva factura' : 'Editar factura'}"/>
<c:set var="moduloActivo" value="facturas"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Emitir factura' : 'Actualizar factura FAC-' += registro.idFactura}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/facturas">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idFactura}">
        </c:if>

        <div class="formulario-rejilla">
            <div class="campo campo--completo ${not empty errores.idPedido ? 'campo--error' : ''}">
                <label for="idPedido">Pedido <span class="obligatorio">*</span></label>
                <select id="idPedido" name="idPedido" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="pedido" items="${pedidos}">
                        <option value="${pedido.idPedido}" ${pedido.idPedido == registro.idPedido ? 'selected' : ''}>
                            #${pedido.idPedido} · <c:out value="${pedido.nombreCliente}"/> ·
                            <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="$ " maxFractionDigits="0"/>
                        </option>
                    </c:forEach>
                </select>
                <p class="campo__ayuda">Un pedido solo puede tener una factura.</p>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idPedido"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.fechaEmision ? 'campo--error' : ''}">
                <label for="fechaEmision">Fecha de emisión <span class="obligatorio">*</span></label>
                <input type="date" id="fechaEmision" name="fechaEmision" required value="${registro.fechaEmision}">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="fechaEmision"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.montoTotal ? 'campo--error' : ''}">
                <label for="montoTotal">Monto total (COP) <span class="obligatorio">*</span></label>
                <input type="number" id="montoTotal" name="montoTotal" min="0" step="0.01" required
                       value="${registro.montoTotal}" placeholder="Ej. 389900">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="montoTotal"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/facturas">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
