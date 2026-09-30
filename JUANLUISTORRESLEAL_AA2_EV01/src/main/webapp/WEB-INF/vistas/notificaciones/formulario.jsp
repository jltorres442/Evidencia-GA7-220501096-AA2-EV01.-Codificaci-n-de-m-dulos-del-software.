<%--
    VISTA: notificaciones/formulario.jsp
    Creación y edición de una notificación. Recibe "registro", "esNuevo",
    "errores" y el catálogo "transportistas".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nueva notificación' : 'Editar notificación'}"/>
<c:set var="moduloActivo" value="notificaciones"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Enviar notificación' : 'Actualizar notificación #' += registro.idNotificacion}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/notificaciones">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idNotificacion}">
        </c:if>

        <div class="formulario-rejilla">
            <div class="campo ${not empty errores.idTransportista ? 'campo--error' : ''}">
                <label for="idTransportista">Transportista <span class="obligatorio">*</span></label>
                <select id="idTransportista" name="idTransportista" required>
                    <option value="">— Seleccione —</option>
                    <c:forEach var="transportista" items="${transportistas}">
                        <option value="${transportista.idTransportista}"
                                ${transportista.idTransportista == registro.idTransportista ? 'selected' : ''}>
                            <c:out value="${transportista.nombre}"/> · <c:out value="${transportista.licencia}"/>
                        </option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="idTransportista"/>
                </jsp:include>
            </div>

            <div class="campo ${not empty errores.fechaHora ? 'campo--error' : ''}">
                <label for="fechaHora">Fecha y hora <span class="obligatorio">*</span></label>
                <input type="datetime-local" id="fechaHora" name="fechaHora" required value="${registro.fechaHora}">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="fechaHora"/>
                </jsp:include>
            </div>

            <div class="campo campo--completo ${not empty errores.mensaje ? 'campo--error' : ''}">
                <label for="mensaje">Mensaje <span class="obligatorio">*</span></label>
                <textarea id="mensaje" name="mensaje" maxlength="2000" required
                          placeholder="Ej. Se le asignó un nuevo envío para hoy.">${fn:escapeXml(registro.mensaje)}</textarea>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="mensaje"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/notificaciones">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
