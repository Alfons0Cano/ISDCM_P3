<%-- 
    Document   : navbar
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <div class="d-flex align-items-center">
            <button class="navbar-toggler me-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <i class="bi bi-list"></i>
            </button>
            <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/videos/lista">
                <i class="bi bi-play-btn-fill me-2"></i>
                VideoWeb
            </a>
        </div>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/videos/lista">
                        <i class="bi bi-collection-play"></i>
                        <span>Explorar</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/videos/mis-videos">
                        <i class="bi bi-person-video"></i>
                        <span>Mis Videos</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center" href="${pageContext.request.contextPath}/videos/registro">
                        <i class="bi bi-plus-circle"></i>
                        <span>Subir</span>
                    </a>
                </li>
            </ul>
            
            <div class="navbar-nav">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle me-2"></i>
                        <span>${sessionScope.username}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li>
                            <a class="dropdown-item d-flex align-items-center" href="${pageContext.request.contextPath}/usuarios/logout">
                                <i class="bi bi-box-arrow-right"></i>
                                <span>Cerrar sesión</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>