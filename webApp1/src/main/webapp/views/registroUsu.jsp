<%-- 
    Document   : registroUsu
    Created on : Feb 27, 2025
    Author     : isdcm
    Modified   : June 4, 2025 - Updated to reflect that only user registration functionality is available
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Usuario</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
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
                            <h3>REGISTRO DE USUARIO</h3>
                        </div>
                        <div class="card-body">
                            <h5 class="text-center mb-4">Crea tu cuenta</h5>                            <div class="alert alert-info">
                                <i class="bi bi-info-circle-fill me-2"></i>
                                Solo está disponible la funcionalidad de registro de usuarios.
                            </div>
                            
                            <% if(request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>
                            
                            <% 
                               // Verificar si hay mensaje en request o en session
                               String message = (String)request.getAttribute("message");
                               if(message == null && session.getAttribute("message") != null) {
                                   message = (String)session.getAttribute("message");
                                   session.removeAttribute("message"); // limpiar para no mostrar repetidamente
                               }
                               if(message != null) { 
                            %>
                                <div class="alert alert-success">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <%= message %>
                                </div>
                            <% } %>
                            
                            <form action="${pageContext.request.contextPath}/usuarios/registro" method="post">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="nombre" class="form-label">Nombre</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Su nombre" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="apellidos" class="form-label">Apellidos</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="apellidos" name="apellidos" placeholder="Sus apellidos" required>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="mail" class="form-label">Correo electrónico</label>
                                    <div class="input-group">
                                        <input type="email" class="form-control" id="mail" name="mail" placeholder="ejemplo@correo.com" required>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="username" class="form-label">Nombre de usuario</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="username" name="username" placeholder="Elija un nombre de usuario" required>
                                    </div>
                                </div>
                                
                                <div class="mb-4">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <div class="input-group">
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
                    <div class="footer">
                        <small>&copy; 2025 VideoWeb. Todos los derechos reservados.</small>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>