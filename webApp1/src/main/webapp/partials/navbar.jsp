<%-- 
    Document   : navbar
    Created on : Feb 27, 2025
    Author     : isdcm
    Modified   : June 4, 2025 - Removed all functionalities except user registration
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <div class="d-flex align-items-center">
            <button class="navbar-toggler me-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <i class="bi bi-list"></i>
            </button>
            <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/">
                <i class="bi bi-person-plus-fill me-2"></i>
                Registro de Usuario
            </a>
        </div>

        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Solo se mantiene la funcionalidad de registro de usuarios -->
        </div>
    </div>
</nav>