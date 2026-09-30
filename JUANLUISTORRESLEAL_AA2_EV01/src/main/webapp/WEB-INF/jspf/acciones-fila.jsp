<%--
    FRAGMENTO DINÁMICO: acciones-fila.jsp
    Botones "Editar" y "Eliminar" de cada fila de un listado.

    Parámetros (<jsp:param>):
      - ruta        : ruta del módulo, p. ej. /clientes
      - id          : llave primaria del registro
      - descripcion : texto para el cuadro de confirmación

    "Editar" es un enlace GET (solo consulta datos); "Eliminar" es un
    formulario POST porque modifica la base de datos.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:url var="urlEditar" value="${param.ruta}">
    <c:param name="accion" value="editar"/>
    <c:param name="id" value="${param.id}"/>
</c:url>
<a class="boton boton--contorno boton--pequeno" href="${urlEditar}">Editar</a>
<form method="post" action="${raiz}${param.ruta}"
      data-confirmar="¿Seguro que desea eliminar ${fn:escapeXml(param.descripcion)}? Esta acción no se puede deshacer.">
    <input type="hidden" name="tokenCsrf" value="${sessionScope.tokenCsrf}">
    <input type="hidden" name="accion" value="eliminar">
    <input type="hidden" name="id" value="${fn:escapeXml(param.id)}">
    <button type="submit" class="boton boton--peligro boton--pequeno">Eliminar</button>
</form>
