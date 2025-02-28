<%-- 
    Document   : playVideo
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${video.titulo} - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    </head>
    <body>
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <div class="row">
                <div class="col-md-8 offset-md-2">
                    <div class="card">
                        <div class="card-header bg-dark text-white">
                            <h3>${video.titulo}</h3>
                        </div>
                        <div class="card-body">
                            <div class="text-center mb-4">
                                <c:choose>
                                    <c:when test="${not empty video.url}">
                                        <video width="100%" controls>
                                            <source src="${video.url}" type="video/${video.formato}">
                                            Tu navegador no soporta la reproducción de videos.
                                        </video>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-warning">
                                            Este video no tiene una URL de reproducción disponible.
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Fecha:</strong> ${video.fechaCreacion}</p>
                                    <p><strong>Duración:</strong> ${video.duracion}</p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Autor:</strong> ${video.autor}</p>
                                    <p><strong>Reproducciones:</strong> ${video.reproducciones}</p>
                                </div>
                            </div>
                            
                            <div class="mt-3">
                                <h5>Descripción</h5>
                                <p>${video.descripcion}</p>
                            </div>
                            
                            <div class="mt-4 text-center">
                                <a href="${pageContext.request.contextPath}/videos/lista" class="btn btn-outline-primary">
                                    <i class="bi bi-arrow-left"></i> Volver al listado
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>