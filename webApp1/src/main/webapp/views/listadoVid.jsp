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
    </head>
    <body>
        <jsp:include page="/WEB-INF/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <div class="row mb-4">
                <div class="col-md-8">
                    <h2>${misVideos ? 'Mis Videos' : 'Listado de Videos'}</h2>
                </div>
                <div class="col-md-4 text-end">
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
                    <a href="${pageContext.request.contextPath}/videos/registro" class="btn btn-success">
                        <i class="bi bi-plus-circle"></i> Nuevo Video
                    </a>
                </div>
            </div>
            
            <!-- Filtros de búsqueda -->
            <div class="card mb-4">
                <div class="card-header bg-light">
                    <i class="bi bi-funnel"></i> Filtrar videos
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/videos/lista" method="get" class="row g-3">
                        <div class="col-md-4">
                            <label for="titulo" class="form-label">Título</label>
                            <input type="text" class="form-control" id="titulo" name="titulo" value="${param.titulo}">
                        </div>
                        <div class="col-md-4">
                            <label for="autor" class="form-label">Autor</label>
                            <input type="text" class="form-control" id="autor" name="autor" value="${param.autor}">
                        </div>
                        <div class="col-md-3">
                            <label for="fecha" class="form-label">Fecha</label>
                            <input type="date" class="form-control" id="fecha" name="fecha" value="${param.fecha}">
                        </div>
                        <div class="col-md-1 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary w-100">Buscar</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Listado de videos -->
            <div class="card">
                <div class="card-header bg-light">
                    <i class="bi bi-film"></i> Videos
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty videos}">
                            <div class="alert alert-info">
                                No hay videos disponibles.
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
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${videos}" var="video">
                                            <tr>
                                                <td>${video.titulo}</td>
                                                <td>${video.autor}</td>
                                                <td>${video.fechaCreacion}</td>
                                                <td>${video.duracion}</td>
                                                <td>${video.reproducciones}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/videos/play/${video.id}" class="btn btn-sm btn-primary">
                                                        <i class="bi bi-play-circle"></i> Ver
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