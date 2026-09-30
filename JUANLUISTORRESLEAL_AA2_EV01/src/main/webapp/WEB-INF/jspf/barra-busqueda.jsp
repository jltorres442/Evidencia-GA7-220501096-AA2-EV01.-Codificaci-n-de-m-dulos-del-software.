<%--
    FRAGMENTO DINÁMICO: barra-busqueda.jsp
    Formulario GET de consulta que envía el parámetro "q" al servlet del módulo.

    Parámetros (<jsp:param>):
      - ruta        : ruta del módulo, p. ej. /clientes
      - ayuda       : texto de ejemplo dentro del campo de búsqueda
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<form class="barra-herramientas" method="get" action="${raiz}${param.ruta}" role="search">
    <div class="buscador">
        <span aria-hidden="true">&#128269;</span>
        <input type="search" name="q" value="${fn:escapeXml(requestScope.filtro)}"
               placeholder="${fn:escapeXml(param.ayuda)}" aria-label="Buscar">
        <button type="submit" class="boton boton--azul boton--pequeno">Buscar</button>
    </div>
    <span class="texto-secundario">
        ${fn:length(requestScope.registros)} registro(s)
        <c:if test="${not empty requestScope.filtro}">
            · <a href="${raiz}${param.ruta}">Limpiar filtro</a>
        </c:if>
    </span>
</form>
