<%-- 
    Document   : listadoVid
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
        
        <div class="container-fluid px-4">
            <!-- Filter section -->
            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/videos/lista" method="get" class="row g-3">
                    <div class="col-md-5 col-lg-6">
                        <div class="input-group">
                            <input type="text" class="form-control" id="titulo" name="titulo" value="${param.titulo}" placeholder="Buscar por título...">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control" id="autor" name="autor" value="${param.autor}" placeholder="Autor">
                        </div>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-calendar"></i></span>
                            <input type="date" class="form-control" id="fecha" name="fecha" value="${param.fecha}">
                        </div>
                    </div>
                </form>
            </div>

            <!-- Video grid -->
            <c:choose>
                <c:when test="${empty videos}">
                    <div class="alert" role="alert">
                        <i class="bi bi-camera-reels" style="font-size: 2rem; display: block; margin-bottom: 1rem;"></i>
                        <h4>No hay videos disponibles</h4>
                        <p class="mb-0">¿Por qué no empiezas <a href="${pageContext.request.contextPath}/videos/registro" class="alert-link">subiendo un video</a>?</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="video-grid">
                        <c:forEach items="${videos}" var="video">
                            <a href="${pageContext.request.contextPath}/videos/play/${video.id}" class="video-card-link">
                                <div class="video-card">
                                    <div class="video-thumbnail">
                                        <div class="thumbnail-placeholder">
                                            <i class="bi bi-play-circle-fill"></i>
                                        </div>
                                        <span class="video-duration">${video.duracion}</span>
                                    </div>
                                    <div class="video-info">
                                        <div class="channel-avatar">
                                            ${fn:substring(video.autor, 0, 1).toUpperCase()}
                                        </div>
                                        <div class="video-info-text">
                                            <h3 class="video-title">${video.titulo}</h3>
                                            <div class="video-details">
                                                <span class="video-author">${video.autor}</span>
                                                <div class="video-stats">
                                                    <span class="video-views">${video.reproducciones} visualizaciones</span>
                                                    <span class="video-date">${video.fechaCreacion}</span>
                                                </div>
                                            </div>
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
        <script>
            // Add event listeners to filter chips
            document.querySelectorAll('.filter-chip').forEach(chip => {
                chip.addEventListener('click', function() {
                    // Remove active class from all chips
                    document.querySelectorAll('.filter-chip').forEach(c => {
                        c.classList.remove('active');
                    });
                    // Add active class to clicked chip
                    this.classList.add('active');
                    // Aquí se podría implementar el filtrado real
                });
            });
        </script>
    </body>
</html>