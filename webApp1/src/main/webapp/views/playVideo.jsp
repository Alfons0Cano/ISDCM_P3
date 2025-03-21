<%-- 
    Document   : playVideo
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${video.titulo} - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/playVid.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container video-page-container mt-4">
            <!-- Video Player Section -->
            <div class="row">
                <div class="col-lg-8">
                    <div class="video-container">
                        <c:choose>
                            <c:when test="${not empty video.url}">
                                <div class="video-player">
                                    <video controls autoplay>
                                        <source src="${video.url}" type="video/${video.formato}">
                                        Tu navegador no soporta la reproducción de videos.
                                    </video>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-warning">
                                    Este video no tiene una URL de reproducción disponible.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Video Information Section -->
                    <div class="video-info-container">
                        <h1 class="video-title">${video.titulo}</h1>
                        
                        <div class="video-stats">
                            <div class="video-views-date">
                                <span><i class="bi bi-eye"></i> ${video.reproducciones} reproducciones</span>
                                <span><i class="bi bi-calendar"></i> ${video.fechaCreacion}</span>
                            </div>
                            
                            <div class="video-duration">
                                <i class="bi bi-clock"></i> ${video.duracion}
                            </div>
                        </div>
                        
                        <!-- Like and Share buttons -->
                        <div class="video-actions">
                            <button class="action-button">
                                <i class="bi bi-hand-thumbs-up"></i> Me gusta
                            </button>
                            <button class="action-button">
                                <i class="bi bi-hand-thumbs-down"></i> No me gusta
                            </button>
                            <button class="action-button">
                                <i class="bi bi-share"></i> Compartir
                            </button>
                            <button class="action-button">
                                <i class="bi bi-download"></i> Descargar
                            </button>
                            <button class="action-button">
                                <i class="bi bi-three-dots"></i>
                            </button>
                        </div>
                        
                        <!-- Author Information -->
                        <div class="video-author">
                            <div class="author-avatar">
                                <i class="bi bi-person-fill"></i>
                            </div>
                            <div class="author-info">
                                <div class="author-name">${video.autor}</div>
                                <div class="subscribers">1.2K suscriptores</div>
                            </div>
                            <button class="subscribe-button">Suscribirse</button>
                        </div>
                        
                        <!-- Video Description -->
                        <div class="video-description">
                            <p>${video.descripcion}</p>
                            
                            <div class="video-details">
                                <div class="detail-item">
                                    <i class="bi bi-film"></i>
                                    <span class="detail-label">Formato:</span>
                                    <span>${video.formato}</span>
                                </div>
                                <div class="detail-item">
                                    <i class="bi bi-clock-history"></i>
                                    <span class="detail-label">Subido:</span>
                                    <span>${video.fechaCreacion}</span>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Comments Section -->
                        <div class="comments-section">
                            <div class="comments-header">
                                <div class="comments-count">124 comentarios</div>
                                <div class="sort-dropdown">
                                    <i class="bi bi-filter"></i>
                                    <span>Ordenar por</span>
                                </div>
                            </div>
                            
                            <div class="comment-form">
                                <div class="user-avatar">
                                    <i class="bi bi-person-fill"></i>
                                </div>
                                <input type="text" class="comment-input" placeholder="Añadir un comentario...">
                            </div>
                            
                            <!-- Sample comments would go here -->
                        </div>
                        
                        <a href="${pageContext.request.contextPath}/videos/lista" class="back-button">
                            <i class="bi bi-arrow-left"></i> Volver al listado
                        </a>
                    </div>
                </div>
                
                <!-- Recommendations Sidebar -->
                <div class="col-lg-4">
                    <div class="recommendations">
                        <c:forEach var="recommendedVideo" items="${recommendations}">
                            <a href="${pageContext.request.contextPath}/videos/play/${recommendedVideo.id}" class="recommendation-item text-decoration-none">
                                <div class="recommendation-thumbnail">
                                    <div style="background-color: #1f1f1f; height: 100%; width: 100%; display: flex; align-items: center; justify-content: center;">
                                        <i class="bi bi-play-circle text-light" style="font-size: 2rem;"></i>
                                    </div>
                                </div>
                                <div class="recommendation-info">
                                    <div class="recommendation-title">${recommendedVideo.titulo}</div>
                                    <div class="recommendation-channel">${recommendedVideo.autor}</div>
                                    <div class="recommendation-metadata">
                                        <span><i class="bi bi-eye"></i> ${recommendedVideo.reproducciones} reproducciones</span>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                        
                        <c:if test="${empty recommendations}">
                            <div class="alert alert-info">
                                No hay más videos disponibles para recomendar.
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>