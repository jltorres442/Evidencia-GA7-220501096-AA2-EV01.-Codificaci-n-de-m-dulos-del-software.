<%--
    VISTA: vendedores/lista.jsp
    Consulta de vendedores (GET /vendedores?q=...). Recibe "registros" y "filtro".
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Vendedores"/>
<c:set var="moduloActivo" value="vendedores"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Vendedores</h2>
        <p>Asesores comerciales que registran los pedidos.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/vendedores?accion=nuevo">+ Nuevo vendedor</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/vendedores"/>
    <jsp:param name="ayuda" value="Buscar por nombre, correo, identificación o teléfono"/>
</jsp:include>

<div class="tabla-contenedor">
    <table class="tabla">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Identificación</th>
            <th>Contacto</th>
            <th>Registra pedidos</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="vendedor" items="${registros}">
            <tr>
                <td>${vendedor.idVendedor}</td>
                <td><strong><c:out value="${vendedor.nombre}"/></strong></td>
                <td><c:out value="${vendedor.numeroIdentificacion}"/></td>
                <td>
                    <c:out value="${vendedor.email}"/>
                    <span class="secundario"><c:out value="${vendedor.telefono}"/></span>
                </td>
                <td>
                    <span class="etiqueta etiqueta--${vendedor.registroPedido ? 'si' : 'no'}">
                        ${vendedor.registroPedido ? 'HABILITADO' : 'INHABILITADO'}
                    </span>
                </td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/vendedores"/>
                        <jsp:param name="id" value="${vendedor.idVendedor}"/>
                        <jsp:param name="descripcion" value="al vendedor ${vendedor.nombre}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="6" class="tabla-vacia">No se encontraron vendedores.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
