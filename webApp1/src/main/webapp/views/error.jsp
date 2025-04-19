<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error - VideoWeb</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f5f5f5;
                margin: 0;
                padding: 20px;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
            }
            .error-container {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                max-width: 600px;
                width: 100%;
            }
            .error-header {
                color: #dc3545;
                margin-bottom: 20px;
            }
            .error-message {
                margin-bottom: 20px;
                color: #333;
            }
            .back-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s;
            }
            .back-button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="error-container">
            <h1 class="error-header">Ha ocurrido un error</h1>
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
            <a href="${pageContext.request.contextPath}/videos/lista" class="back-button">Volver a la lista de videos</a>
        </div>
    </body>
</html> 