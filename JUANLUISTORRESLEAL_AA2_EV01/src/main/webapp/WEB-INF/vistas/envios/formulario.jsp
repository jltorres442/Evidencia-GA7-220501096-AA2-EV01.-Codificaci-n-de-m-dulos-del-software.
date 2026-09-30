<%--
    VISTA: envios/formulario.jsp
    Creación y edición de un envío. Recibe "registro", "esNuevo", "errores"
    y los catálogos "pedidos" y "responsablesLogistica".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo envío' : 'Editar envío'}"/>
<c:set var="moduloActivo" value="envios"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Programar envío' : 'Actualizar envío #' += registro.idEnvio}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/envios">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idEnvio}">
        </c:if>

        <div class="formulario-rejilla">
            <div class="campo ${not empty errores.idPedido ? 'campo--error' : ''}">
                <label for="idPedido">Pedido <span class="obligatorio">*</span></label>
                <select id="idPedido" name="idPedido" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="pedido" items="${pedidos}">
                        <option value="${pedido.idPedido}" ${pedido.idPedido == registro.idPedido ? 'selected' : ''}>
                            #${pedido.idPedido} · <c:out value="${pedido.nombreCliente}"/>
                        </option>
                    </c:forEach>
                </select>
                <p class="campo__ayuda">Un pedido solo puede tener un envío.</p>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idPedido"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.idLogistica ? 'campo--error' : ''}">
                <label for="idLogistica">Responsable de logística <span class="obligatorio">*</span></label>
                <select id="idLogistica" name="idLogistica" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="responsable" items="${responsablesLogistica}">
                        <option value="${responsable.idLogistica}" ${responsable.idLogistica == registro.idLogistica ? 'selected' : ''}>
                            <c:out value="${responsable.nombre}"/>
                        </option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idLogistica"/>
                </jsp:include>
            </div>

            <div class="campo campo--completo ${not empty errores.direccionEntrega ? 'campo--error' : ''}">
                <label for="direccionEntrega">Dirección de entrega <span class="obligatorio">*</span></label>
                <input type="text" id="direccionEntrega" name="direccionEntrega" maxlength="250" required
                       value="${fn:escapeXml(registro.direccionEntrega)}" placeholder="Ej. Cl. 72 # 10-34, Bogotá">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="direccionEntrega"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.fechaEnvio ? 'campo--error' : ''}">
                <label for="fechaEnvio">Fecha de envío <span class="obligatorio">*</span></label>
                <input type="date" id="fechaEnvio" name="fechaEnvio" required value="${registro.fechaEnvio}">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="fechaEnvio"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.estado ? 'campo--error' : ''}">
                <label for="estado">Estado <span class="obligatorio">*</span></label>
                <select id="estado" name="estado" required>
                    <c:forEach var="estado" items="${estadosEnvio}">
                        <option value="${estado}" ${estado == registro.estado ? 'selected' : ''}>${fn:replace(estado, '_', ' ')}</option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="estado"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/envios">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
