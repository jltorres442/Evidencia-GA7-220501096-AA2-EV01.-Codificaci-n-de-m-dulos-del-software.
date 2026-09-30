<%--
    VISTA: clientes/lista.jsp
    Consulta de clientes (GET /clientes?q=...). Recibe:
      - registros : List<Cliente>
      - filtro    : texto buscado
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="tituloPagina" value="Clientes"/>
<c:set var="moduloActivo" value="clientes"/>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>

<section class="banda-seccion">
    <div>
        <h2>Clientes</h2>
        <p>Personas que realizan pedidos en Homecenter.</p>
    </div>
    <a class="boton boton--banda" href="${raiz}/clientes?accion=nuevo">+ Nuevo cliente</a>
</section>

<jsp:include page="/WEB-INF/jspf/barra-busqueda.jsp">
    <jsp:param name="ruta" value="/clientes"/>
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
            <th>Dirección</th>
            <th>Registro</th>
            <th class="acciones">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cliente" items="${registros}">
            <tr>
                <td>${cliente.idCliente}</td>
                <td><strong><c:out value="${cliente.nombre}"/></strong></td>
                <td><c:out value="${cliente.numeroIdentificacion}"/></td>
                <td>
                    <c:out value="${cliente.email}"/>
                    <span class="secundario"><c:out value="${cliente.telefono}"/></span>
                </td>
                <td><c:out value="${cliente.direccion}"/></td>
                <td class="fecha">${fn:substring(cliente.fechaRegistro, 0, 10)}</td>
                <td class="acciones">
                    <jsp:include page="/WEB-INF/jspf/acciones-fila.jsp">
                        <jsp:param name="ruta" value="/clientes"/>
                        <jsp:param name="id" value="${cliente.idCliente}"/>
                        <jsp:param name="descripcion" value="el cliente ${cliente.nombre}"/>
                    </jsp:include>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registros}">
            <tr><td colspan="7" class="tabla-vacia">No se encontraron clientes.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>
