<%-- 
    Document   : registroVid
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Video - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registroVid.css">
    </head>
    <body class="form-page">
        <jsp:include page="/partials/navbar.jsp" />
        
        <div class="container mt-4">
            <!-- Page header -->
            <div class="page-header d-flex align-items-center">
                <i class="bi bi-film me-3" style="font-size: 2rem; color: #3a7bd5;"></i>
                <h3>Registro de Video</h3>
            </div>
            
            <div class="row justify-content-center">
                <div class="col-md-9">
                    <div class="card form-card">
                        <div class="card-header text-center">
                            <h3>Nuevo Video</h3>
                        </div>
                        <div class="card-body">
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                            
                            <% if(request.getAttribute("success") != null) { %>
                                <div class="alert alert-success">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <%= request.getAttribute("success") %>
                                </div>
                            <% } %>
                            
                            <form action="${pageContext.request.contextPath}/videos/registro" method="post">
                                <div class="form-section">
                                    <h5><i class="bi bi-info-circle me-2"></i>Información Básica</h5>
                                    
                                    <div class="mb-3">
                                        <label for="titulo" class="form-label">Título</label>
                                        <div class="input-group">
                                            <span class="input-icon"><i class="bi bi-type"></i></span>
                                            <input type="text" class="form-control" id="titulo" name="titulo" required>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="descripcion" class="form-label">Descripción</label>
                                        <div class="position-relative">
                                            <span class="input-icon-static"><i class="bi bi-card-text"></i></span>
                                            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" style="padding-left: 3rem;"></textarea>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-section">
                                    <h5><i class="bi bi-clock-history me-2"></i>Detalles Técnicos</h5>
                                    
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="fecha" class="form-label">Fecha de creación</label>
                                            <div class="input-group">
                                                <span class="input-icon"><i class="bi bi-calendar-date"></i></span>
                                                <input type="date" class="form-control" id="fecha" name="fecha" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="duracion" class="form-label">Duración (HH:MM:SS)</label>
                                            <div class="input-group">
                                                <span class="input-icon"><i class="bi bi-stopwatch"></i></span>
                                                <input type="text" class="form-control" id="duracion" name="duracion" placeholder="00:30:00" required>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="formato" class="form-label">Formato</label>
                                            <div class="position-relative">
                                                <span class="input-icon-static"><i class="bi bi-file-earmark-play"></i></span>
                                                <select class="form-select" id="formato" name="formato" style="padding-left: 3rem;">
                                                    <option value="mp4">MP4</option>
                                                    <option value="avi">AVI</option>
                                                    <option value="mkv">MKV</option>
                                                    <option value="mov">MOV</option>
                                                    <option value="wmv">WMV</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="url" class="form-label">URL del video</label>
                                            <div class="input-group">
                                                <span class="input-icon"><i class="bi bi-link-45deg"></i></span>
                                                <input type="url" class="form-control" id="url" name="url" placeholder="https://...">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="d-grid gap-2 mt-4">
                                    <button type="submit" class="btn btn-primary btn-submit">
                                        <i class="bi bi-cloud-upload me-2"></i>Registrar Video
                                    </button>
                                </div>
                                
                                <div class="text-center mt-3">
                                    <a href="${pageContext.request.contextPath}/videos/lista" class="register-link">
                                        <i class="bi bi-arrow-left me-1"></i> Volver al listado de videos
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>