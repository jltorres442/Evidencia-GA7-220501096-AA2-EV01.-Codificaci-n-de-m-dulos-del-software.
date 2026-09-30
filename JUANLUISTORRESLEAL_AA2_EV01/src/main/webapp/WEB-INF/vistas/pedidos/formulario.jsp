<%--
    VISTA: pedidos/formulario.jsp
    Creación y edición de un pedido. Recibe "registro", "esNuevo", "errores"
    y los catálogos "clientes" y "vendedores". Los estados posibles vienen
    del ámbito de aplicación (${estadosPedido}), publicados por InicializadorAplicacion.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo pedido' : 'Editar pedido'}"/>
<c:set var="moduloActivo" value="pedidos"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Registrar pedido' : 'Actualizar pedido #' += registro.idPedido}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/pedidos">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idPedido}">
        </c:if>

        <div class="formulario-rejilla">
            <div class="campo ${not empty errores.idCliente ? 'campo--error' : ''}">
                <label for="idCliente">Cliente <span class="obligatorio">*</span></label>
                <select id="idCliente" name="idCliente" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="cliente" items="${clientes}">
                        <option value="${cliente.idCliente}" ${cliente.idCliente == registro.idCliente ? 'selected' : ''}>
                            <c:out value="${cliente.nombre}"/> · <c:out value="${cliente.numeroIdentificacion}"/>
                        </option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idCliente"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.idVendedor ? 'campo--error' : ''}">
                <label for="idVendedor">Vendedor <span class="obligatorio">*</span></label>
                <select id="idVendedor" name="idVendedor" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="vendedor" items="${vendedores}">
                        <option value="${vendedor.idVendedor}" ${vendedor.idVendedor == registro.idVendedor ? 'selected' : ''}>
                            <c:out value="${vendedor.nombre}"/>
                        </option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idVendedor"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.fecha ? 'campo--error' : ''}">
                <label for="fecha">Fecha <span class="obligatorio">*</span></label>
                <input type="date" id="fecha" name="fecha" required value="${registro.fecha}">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="fecha"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.total ? 'campo--error' : ''}">
                <label for="total">Total (COP) <span class="obligatorio">*</span></label>
                <input type="number" id="total" name="total" min="0" step="0.01" required
                       value="${registro.total}" placeholder="Ej. 389900">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="total"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.estado ? 'campo--error' : ''}">
                <label for="estado">Estado <span class="obligatorio">*</span></label>
                <select id="estado" name="estado" required>
                    <c:forEach var="estado" items="${estadosPedido}">
                        <option value="${estado}" ${estado == registro.estado ? 'selected' : ''}>${fn:replace(estado, '_', ' ')}</option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="estado"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/pedidos">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
