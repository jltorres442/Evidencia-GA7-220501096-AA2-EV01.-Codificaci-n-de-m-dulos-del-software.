<%--
    FRAGMENTO DINÁMICO: error-campo.jsp
    Muestra el mensaje de validación de un campo. Se invoca así:

      <jsp:include page="/WEB-INF/jspf/error-campo.jsp">
          <jsp:param name="campo" value="email"/>
      </jsp:include>

    El valor de <jsp:param> llega como ${param.campo} y se usa para buscar
    el mensaje en el mapa "errores" que publica CrudServlet.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="mensajeCampo" value="${requestScope.errores[param.campo]}"/>
<c:if test="${not empty mensajeCampo}">
    <p class="campo__error" id="error-${param.campo}"><c:out value="${mensajeCampo}"/></p>
</c:if>
