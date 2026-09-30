<%--
    VISTA: incidencias/formulario.jsp
    Creación y edición de una incidencia. Recibe "registro", "esNuevo",
    "errores" y el catálogo "envios".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nueva incidencia' : 'Editar incidencia'}"/>
<c:set var="moduloActivo" value="incidencias"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Reportar incidencia' : 'Actualizar incidencia #' += registro.idIncidencia}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/incidencias">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idIncidencia}">
        </c:if>

        <div class="formulario-rejilla">
            <div class="campo ${not empty errores.idEnvio ? 'campo--error' : ''}">
                <label for="idEnvio">Envío afectado <span class="obligatorio">*</span></label>
                <select id="idEnvio" name="idEnvio" required>
                    <option value="">— Seleccione —</option>
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

            <div class="campo ${not empty errores.estado ? 'campo--error' : ''}">
                <label for="estado">Estado <span class="obligatorio">*</span></label>
                <select id="estado" name="estado" required>
                    <c:forEach var="estado" items="${estadosIncidencia}">
                        <option value="${estado}" ${estado == registro.estado ? 'selected' : ''}>${fn:replace(estado, '_', ' ')}</option>
                    </c:forEach>
                </select>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="estado"/>
                </jsp:include>
            </div>

            <div class="campo campo--completo ${not empty errores.descripcion ? 'campo--error' : ''}">
                <label for="descripcion">Descripción <span class="obligatorio">*</span></label>
                <textarea id="descripcion" name="descripcion" maxlength="2000" required
                          placeholder="Ej. Cliente no presente, dirección incorrecta, paquete dañado...">${fn:escapeXml(registro.descripcion)}</textarea>
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="descripcion"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/incidencias">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
