<%-- 
    Document   : registroUsu
    Created on : Feb 27, 2025
    Author     : isdcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Usuario - VideoWeb</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <style>
            /* Override any animation classes for registro page */
            .container {
                opacity: 1;
                visibility: visible;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="logo">
                        <i class="bi bi-person-plus-fill"></i>
                    </div>
                    <div class="card">
                        <div class="card-header text-center">
                            <h3>REGISTRO DE USUARIO</h3>
                        </div>
                        <div class="card-body">
                            <h5 class="text-center mb-4">Crea tu cuenta</h5>
                            
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                            
                            <form action="${pageContext.request.contextPath}/usuarios/registro" method="post">
                                <!-- Form content remains unchanged -->
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="nombre" class="form-label">Nombre</label>
                                        <div class="input-group">
                                            <span class="input-icon"><i class="bi bi-person-fill"></i></span>
                                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Su nombre" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="apellidos" class="form-label">Apellidos</label>
                                        <div class="input-group">
                                            <span class="input-icon"><i class="bi bi-person-badge-fill"></i></span>
                                            <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Sus apellidos" required>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="mail" class="form-label">Correo electrónico</label>
                                    <div class="input-group">
                                        <span class="input-icon"><i class="bi bi-envelope-fill"></i></span>
                                        <input type="email" class="form-control" id="mail" name="mail" placeholder="ejemplo@correo.com" required>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="username" class="form-label">Nombre de usuario</label>
                                    <div class="input-group">
                                        <span class="input-icon"><i class="bi bi-at"></i></span>
                                        <input type="text" class="form-control" id="username" name="username" placeholder="Elija un nombre de usuario" required>
                                    </div>
                                </div>
                                
                                <div class="mb-4">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <div class="input-group">
                                        <span class="input-icon"><i class="bi bi-lock-fill"></i></span>
                                        <input type="password" class="form-control" id="password" name="password" placeholder="Elija una contraseña segura" required>
                                    </div>
                                </div>
                                
                                <div class="d-grid gap-2 mt-4">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-person-check-fill me-2"></i>Registrarse
                                    </button>
                                </div>
                            </form>
                            
                            <div class="mt-4 text-center register-link">
                                <p>¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/usuarios">Iniciar sesión</a></p>
                            </div>
                        </div>
                    </div>
                    <div class="text-center mt-3 text-white">
                        <small>&copy; 2025 VideoWeb. Todos los derechos reservados.</small>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>