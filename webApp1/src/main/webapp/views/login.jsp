<%-- 
    Document   : login
    Created on : Feb 27, 2025
    Author     : isdcm
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar sesión - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>
    <body class="login-page">
        <div class="container login-container">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="card">
                        <div class="card-header text-center">
                            <h3>VIDEOWEB</h3>
                        </div>
                        <div class="card-body">
                            <h5 class="text-center mb-4">Iniciar sesión en su cuenta</h5>
                            
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                            <% if(request.getAttribute("message") != null) { %>
                                <div class="alert alert-success">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <%= request.getAttribute("message") %>
                                </div>
                            <% } %>
                            
                            <form action="${pageContext.request.contextPath}/usuarios/login" method="post">
                                <div class="mb-4">
                                    <label for="username" class="form-label">Nombre de usuario</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="username" name="username" placeholder="Ingrese su nombre de usuario" required>
                                    </div>
                                </div>
                                <div class="mb-4">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="password" name="password" placeholder="Ingrese su contraseña" required>
                                    </div>
                                </div>
                                <div class="d-grid gap-2 mt-4">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar sesión
                                    </button>
                                </div>
                            </form>
                            <div class="mt-4 text-center register-link">
                                <p>¿No tienes una cuenta? <a href="${pageContext.request.contextPath}/usuarios/registro">Regístrate aquí</a></p>
                            </div>
                        </div>
                    </div>
                    <div class="footer">
                        <small>&copy; 2025 VideoWeb. Todos los derechos reservados.</small>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>