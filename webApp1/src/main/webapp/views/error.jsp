<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error - VideoWeb</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
        <style>
            body {
                font-family: 'Roboto', sans-serif;
                background-color: #181818;
                color: #fff;
                min-height: 100vh;
                padding-top: 56px; /* Match navbar height */
            }
            .error-container {
                background-color: #212121;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.2);
                max-width: 600px;
                width: 100%;
                margin: 2rem auto;
                border: 1px solid #303030;
            }
            .error-header {
                color: #dc3545;
                margin-bottom: 1.5rem;
                font-weight: 500;
            }
            .error-message {
                margin-bottom: 1.5rem;
                color: #fff;
                font-size: 1.1rem;
                line-height: 1.5;
            }
            .back-button {
                display: inline-flex;
                align-items: center;
                padding: 0.75rem 1.5rem;
                background-color: #3ea6ff;
                color: #181818;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s;
                font-weight: 500;
            }
            .back-button:hover {
                background-color: #65b5ff;
                color: #181818;
            }
            .error-icon {
                font-size: 3rem;
                color: #dc3545;
                margin-bottom: 1rem;
            }
        </style>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.username}">
                <c:redirect url="/usuarios"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="/partials/navbar.jsp" />
                
                <div class="container">
                    <div class="error-container">
                        <div class="text-center">
                            <i class="bi bi-exclamation-triangle-fill error-icon"></i>
                            <h1 class="error-header">Ha ocurrido un error</h1>
                        </div>
                        <div class="error-message">
                            <%
                                String errorMessage = (String) request.getAttribute("error");
                                if (errorMessage != null) {
                                    out.println(errorMessage);
                                } else if (exception != null) {
                                    out.println("Error interno del servidor: " + exception.getMessage());
                                } else {
                                    out.println("Se ha producido un error inesperado.");
                                }
                            %>
                        </div>
                        <div class="text-center">
                            <a href="${pageContext.request.contextPath}/videos/lista" class="back-button">
                                <i class="bi bi-arrow-left me-2"></i>Volver a la lista de videos
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 