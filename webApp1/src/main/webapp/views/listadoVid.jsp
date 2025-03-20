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
        <title>${misVideos ? 'Mis Videos' : 'Explorar'} - VideoWeb</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listadoVid.css">
    </head>
    <body class="dashboard">
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <!-- Page header with title and action buttons -->
            <div class="page-header d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                    <h2>
                        <i class="bi bi-collection-play me-2"></i>
                        ${misVideos ? 'Mis Videos' : 'Explorar'}
                        <c:if test="${not empty videos}">
                            <span class="badge-count">${videos.size()}</span>
                        </c:if>
                    </h2>
                </div>
                <div class="action-buttons">
                    <c:if test="${!misVideos}">
                        <a href="${pageContext.request.contextPath}/videos/mis-videos" class="btn btn-outline-primary">
                            <i class="bi bi-person-video me-2"></i>Mis videos
                        </a>
                    </c:if>
                    <c:if test="${misVideos}">
                        <a href="${pageContext.request.contextPath}/videos/lista" class="btn btn-outline-primary">
                            <i class="bi bi-collection-play me-2"></i>Explorar
                        </a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/videos/registro" class="btn btn-primary ms-2">
                        <i class="bi bi-plus-circle me-2"></i>Subir video
                    </a>
                </div>
            </div>

            <!-- Filter section -->
            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/videos/lista" method="get" class="row g-3">
                    <div class="col-md-4">
                        <label for="titulo" class="form-label">Título</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-0"><i class="bi bi-search text-light"></i></span>
                            <input type="text" class="form-control" id="titulo" name="titulo" value="${param.titulo}" placeholder="Buscar por título...">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <label for="autor" class="form-label">Autor</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-0"><i class="bi bi-person text-light"></i></span>
                            <input type="text" class="form-control" id="autor" name="autor" value="${param.autor}" placeholder="Buscar por autor...">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <label for="fecha" class="form-label">Fecha</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark border-0"><i class="bi bi-calendar text-light"></i></span>
                            <input type="date" class="form-control" id="fecha" name="fecha" value="${param.fecha}">
                        </div>
                    </div>
                    <div class="col-md-1 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </form>
            </div>

            <!-- Video grid -->
            <c:choose>
                <c:when test="${empty videos}">
                    <div class="alert alert-info" role="alert">
                        <i class="bi bi-camera-reels" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>
                        <h4>No hay videos disponibles</h4>
                        <p class="mb-0">¿Por qué no empiezas <a href="${pageContext.request.contextPath}/videos/registro" class="alert-link">subiendo un video</a>?</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="video-grid">
                        <c:forEach items="${videos}" var="video">
                            <a href="${pageContext.request.contextPath}/videos/play/${video.id}" class="text-decoration-none">
                                <div class="video-card">
                                    <div class="video-thumbnail">
                                        <i class="bi bi-play-circle"></i>
                                    </div>
                                    <div class="video-info">
                                        <h3 class="video-title">${video.titulo}</h3>
                                        <div class="video-author">${video.autor}</div>
                                        <div class="video-metadata">
                                            <span><i class="bi bi-eye me-1"></i>${video.reproducciones}</span>
                                            <span><i class="bi bi-clock me-1"></i>${video.duracion}</span>
                                            <span><i class="bi bi-calendar me-1"></i>${video.fechaCreacion}</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>