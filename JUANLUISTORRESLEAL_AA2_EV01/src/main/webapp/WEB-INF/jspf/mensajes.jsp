<%--
    FRAGMENTO DINÁMICO: mensajes.jsp
    Se incluye con <jsp:include>, por lo que se ejecuta como una página
    independiente que comparte los atributos de la petición. Muestra los
    mensajes de éxito y de error preparados por ServletBase.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty requestScope.mensajeExito}">
    <div class="alerta alerta--exito" role="status">&#10004; <c:out value="${requestScope.mensajeExito}"/></div>
</c:if>
<c:if test="${not empty requestScope.mensajeError}">
    <div class="alerta alerta--error" role="alert">&#9888; <c:out value="${requestScope.mensajeError}"/></div>
</c:if>
