<%--
    Página de bienvenida: redirige al panel de inicio.
    Si el usuario no ha iniciado sesión, AutenticacionFiltro lo enviará a /login.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:redirect url="/inicio"/>
