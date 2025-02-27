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
    </head>
    <body>
        <jsp:include page="/WEB-INF/partials/navbar.jsp" />
        
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header text-center">
                            <h3>Registro de Video</h3>
                        </div>
                        <div class="card-body">
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger">
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                            <form action="${pageContext.request.contextPath}/videos/registro" method="post">
                                <div class="mb-3">
                                    <label for="titulo" class="form-label">Título</label>
                                    <input type="text" class="form-control" id="titulo" name="titulo" required>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="fecha" class="form-label">Fecha de creación</label>
                                        <input type="date" class="form-control" id="fecha" name="fecha" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="duracion" class="form-label">Duración (HH:MM:SS)</label>
                                        <input type="text" class="form-control" id="duracion" name="duracion" placeholder="00:30:00" required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="descripcion" class="form-label">Descripción</label>
                                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3"></textarea>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="formato" class="form-label">Formato</label>
                                        <select class="form-select" id="formato" name="formato">
                                            <option value="mp4">MP4</option>
                                            <option value="avi">AVI</option>
                                            <option value="mkv">MKV</option>
                                            <option value="mov">MOV</option>
                                            <option value="wmv">WMV</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="url" class="form-label">URL del video</label>
                                        <input type="url" class="form-control" id="url" name="url">
                                    </div>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">Registrar Video</button>
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