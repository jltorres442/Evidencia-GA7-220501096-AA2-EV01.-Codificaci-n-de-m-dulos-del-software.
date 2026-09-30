<%--
    VISTA: logistica/formulario.jsp
    Creación y edición de un responsable de logística. Recibe "registro", "esNuevo" y "errores".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo responsable de logística' : 'Editar responsable de logística'}"/>
<c:set var="moduloActivo" value="logistica"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Registrar responsable de logística' : 'Actualizar responsable #' += registro.idLogistica}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/logistica">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idLogistica}">
        </c:if>

        <div class="formulario-rejilla">
            <%@ include file="/WEB-INF/jspf/campos-usuario.jspf" %>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/logistica">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
