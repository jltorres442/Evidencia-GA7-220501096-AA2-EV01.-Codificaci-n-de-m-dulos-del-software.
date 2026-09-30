<%--
    VISTA: clientes/formulario.jsp
    Creación (accion=guardar) y edición (accion=actualizar) de un cliente.
    Recibe:
      - registro : Cliente a mostrar
      - esNuevo  : true si es un registro nuevo
      - errores  : Map campo -> mensaje (solo si hubo errores de validación)
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo cliente' : 'Editar cliente'}"/>
<c:set var="moduloActivo" value="clientes"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Registrar cliente' : 'Actualizar cliente #' += registro.idCliente}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/clientes">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idCliente}">
        </c:if>

        <div class="formulario-rejilla">
            <%@ include file="/WEB-INF/jspf/campos-usuario.jspf" %>

            <div class="campo campo--completo ${not empty errores.direccion ? 'campo--error' : ''}">
                <label for="direccion">Dirección <span class="obligatorio">*</span></label>
                <input type="text" id="direccion" name="direccion" maxlength="200" required
                       value="${fn:escapeXml(registro.direccion)}" placeholder="Ej. Cra. 15 # 93-47, Bogotá">
                <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
                    <jsp:param name="campo" value="direccion"/>
                </jsp:include>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/clientes">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
