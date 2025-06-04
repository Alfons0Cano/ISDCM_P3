<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Test de Conexión BD</title>
</head>
<body>
    <h1>Test de Conexión a Base de Datos</h1>
    
    <%
        String message = "";
        String status = "success";
        
        try {
            String url = "jdbc:derby://localhost:1527/pr2";
            String user = "pr2";
            String password = "pr2";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            message += "<p>✓ Conexión exitosa a la base de datos</p>";
            
            // Verificar tabla Usuarios
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "USUARIOS", null);
            
            if (tables.next()) {
                message += "<p>✓ Tabla USUARIOS encontrada</p>";
                
                // Contar usuarios
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Usuarios");
                if (rs.next()) {
                    int total = rs.getInt("total");
                    message += "<p>Total de usuarios registrados: " + total + "</p>";
                }
                rs.close();
                
                // Mostrar algunos usuarios
                rs = stmt.executeQuery("SELECT ID, Username, Nombre, Mail FROM Usuarios ORDER BY ID DESC");
                message += "<h3>Usuarios registrados:</h3><ul>";
                while (rs.next()) {
                    message += "<li>ID: " + rs.getInt("ID") + 
                              " - Usuario: " + rs.getString("Username") + 
                              " - Nombre: " + rs.getString("Nombre") + 
                              " - Email: " + rs.getString("Mail") + "</li>";
                }
                message += "</ul>";
                rs.close();
                stmt.close();
                
            } else {
                message += "<p>✗ Tabla USUARIOS no encontrada</p>";
                status = "error";
            }
            
            conn.close();
            
        } catch (SQLException e) {
            message = "<p>✗ Error de conexión: " + e.getMessage() + "</p>";
            status = "error";
        }
    %>    <% if (status.equals("success")) { %>
        <div style="padding: 20px; border: 2px solid green; background-color: #d4edda;">
    <% } else { %>
        <div style="padding: 20px; border: 2px solid red; background-color: #f8d7da;">
    <% } %>
        <%= message %>
    </div>
    
    <p><a href="<%= request.getContextPath() %>/usuarios/registro">Volver al registro</a></p>
</body>
</html>
