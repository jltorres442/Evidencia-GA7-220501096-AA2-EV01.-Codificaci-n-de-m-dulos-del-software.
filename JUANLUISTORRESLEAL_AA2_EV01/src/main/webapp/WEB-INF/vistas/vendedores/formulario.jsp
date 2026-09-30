<%--
    VISTA: vendedores/formulario.jsp
    Creación y edición de un vendedor. Recibe "registro", "esNuevo" y "errores".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="${esNuevo ? 'Nuevo vendedor' : 'Editar vendedor'}"/>
<c:set var="moduloActivo" value="vendedores"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>${esNuevo ? 'Registrar vendedor' : 'Actualizar vendedor #' += registro.idVendedor}</h2>
        <p>Los campos marcados con <strong>*</strong> son obligatorios.</p>
    </div>
</section>

<section class="tarjeta">
    <form method="post" action="${raiz}/vendedores">
        <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
        <input type="hidden" name="accion" value="${esNuevo ? 'guardar' : 'actualizar'}">
        <c:if test="${not esNuevo}">
            <input type="hidden" name="id" value="${registro.idVendedor}">
        </c:if>

        <div class="formulario-rejilla">
            <%@ include file="/WEB-INF/jspf/campos-usuario.jspf" %>

            <div class="campo campo--completo">
                <label class="casilla">
                    <input type="checkbox" name="registroPedido" ${registro.registroPedido ? 'checked' : ''}>
                    <span>Habilitado para registrar pedidos</span>
                </label>
            </div>
        </div>

        <div class="acciones-formulario">
            <a class="boton boton--contorno" href="${raiz}/vendedores">Cancelar</a>
            <button type="submit" class="boton boton--primario">${esNuevo ? 'GUARDAR' : 'ACTUALIZAR'}</button>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
