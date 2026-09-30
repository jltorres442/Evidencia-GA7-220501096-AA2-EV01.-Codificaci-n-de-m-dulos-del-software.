<%--
    VISTA: transportistas/formulario.jsp
    Creación y edición de un transportista. Recibe "registro", "esNuevo",
    "errores" y el catálogo "envios" para la lista desplegable.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo transportista' : 'Editar transportista'}"/>
<c:set var="moduloActivo" value="transportistas"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Registrar transportista' : 'Actualizar transportista #' += registro.idTransportista}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/transportistas">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idTransportista}">
        </c:if>

        <div class="formulario-rejilla">
            <%@ include file="/WEB-INF/jspf/campos-usuario.jspf" %>

            <div class="campo ${not empty errores.licencia ? 'campo--error' : ''}">
                <label for="licencia">Licencia de conducción <span class="obligatorio">*</span></label>
                <input type="text" id="licencia" name="licencia" maxlength="50" required
                       value="${fn:escapeXml(registro.licencia)}" placeholder="Ej. C2-1098765432">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="licencia"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.idEnvio ? 'campo--error' : ''}">
                <label for="idEnvio">Envío asignado</label>
                <select id="idEnvio" name="idEnvio">
                    <option value="">— Sin asignar (disponible) —</option>
                    <c:forEach var="envio" items="${envios}">
                        <option value="${envio.idEnvio}" ${envio.idEnvio == registro.idEnvio ? 'selected' : ''}>
                            #${envio.idEnvio} · <c:out value="${envio.direccionEntrega}"/>
                        </option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idEnvio"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/transportistas">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
