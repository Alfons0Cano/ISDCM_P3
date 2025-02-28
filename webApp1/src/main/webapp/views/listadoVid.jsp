<%-- 
    Document   : listadoVid
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${misVideos ? 'Mis Videos' : 'Listado de Videos'} - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listadoVid.css">
    </head>
    <body class="dashboard">
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <!-- Page header with title and action buttons -->
            <div class="page-header mb-4 d-flex align-items-center justify-content-between">
                <div>
                    <h2>
                        <i class="bi bi-collection-play me-2"></i>
                        ${misVideos ? 'Mis Videos' : 'Listado de Videos'}
                        <c:if test="${not empty videos}">
                            <span class="badge-count ms-2">${videos.size()}</span>
                        </c:if>
                    </h2>
                </div>
                <div class="action-buttons">
                    <c:if test="${!misVideos}">
                        <a href="${pageContext.request.contextPath}/videos/mis-videos" class="btn btn-outline-primary">
                            <i class="bi bi-person-video"></i> Ver mis videos
                        </a>
                    </c:if>
                    <c:if test="${misVideos}">
                        <a href="${pageContext.request.contextPath}/videos/lista" class="btn btn-outline-primary">
                            <i class="bi bi-collection-play"></i> Ver todos los videos
                        </a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/videos/registro" class="btn btn-success ms-2">
                        <i class="bi bi-plus-circle"></i> Nuevo Video
                    </a>
                </div>
            </div>
            
            <!-- Filtros de búsqueda -->
            <div class="card">
                <div class="card-header">
                    <i class="bi bi-funnel"></i> Filtrar videos
                </div>
                <div class="card-body filter-section">
                    <form action="${pageContext.request.contextPath}/videos/lista" method="get" class="row g-3">
                        <div class="col-md-4">
                            <label for="titulo" class="form-label">Título</label>
                            <input type="text" class="form-control" id="titulo" name="titulo" value="${param.titulo}" placeholder="Buscar por título...">
                        </div>
                        <div class="col-md-4">
                            <label for="autor" class="form-label">Autor</label>
                            <input type="text" class="form-control" id="autor" name="autor" value="${param.autor}" placeholder="Buscar por autor...">
                        </div>
                        <div class="col-md-3">
                            <label for="fecha" class="form-label">Fecha</label>
                            <input type="date" class="form-control" id="fecha" name="fecha" value="${param.fecha}">
                        </div>
                        <div class="col-md-1 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-search"></i> Buscar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Listado de videos -->
            <div class="card">
                <div class="card-header">
                    <i class="bi bi-film"></i> Videos
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty videos}">
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle me-2"></i>
                                No hay videos disponibles. 
                                <a href="${pageContext.request.contextPath}/videos/registro" class="alert-link">¡Agrega un nuevo video!</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Título</th>
                                            <th>Autor</th>
                                            <th>Fecha</th>
                                            <th>Duración</th>
                                            <th>Reproducciones</th>
                                            <th class="text-center">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${videos}" var="video">
                                            <tr>
                                                <td>
                                                    <strong>${video.titulo}</strong>
                                                </td>
                                                <td>${video.autor}</td>
                                                <td>${video.fechaCreacion}</td>
                                                <td>${video.duracion}</td>
                                                <td>
                                                    <span class="badge bg-light text-dark">
                                                        <i class="bi bi-eye me-1"></i>${video.reproducciones}
                                                    </span>
                                                </td>
                                                <td class="text-center">
                                                    <a href="${pageContext.request.contextPath}/videos/play/${video.id}" 
                                                       class="btn btn-sm btn-view" 
                                                       title="Ver video">
                                                        <i class="bi bi-play-circle"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>