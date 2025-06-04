package isdcm.webapp1.test;

import java.sql.*;

public class DatabaseTest {
    
    public static void main(String[] args) {
        String url = "jdbc:derby://localhost:1527/pr2";
        String username = "pr2";
        String password = "pr2";
        
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("✓ Conexión exitosa a la base de datos");
            
            // Verificar si la tabla Usuarios existe
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "USUARIOS", null);
            
            if (tables.next()) {
                System.out.println("✓ Tabla USUARIOS encontrada");
                
                // Mostrar estructura de la tabla
                ResultSet columns = metaData.getColumns(null, null, "USUARIOS", null);
                System.out.println("Columnas de la tabla USUARIOS:");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    boolean nullable = columns.getBoolean("NULLABLE");
                    System.out.println("  - " + columnName + " (" + dataType + 
                                     (columnSize > 0 ? "(" + columnSize + ")" : "") + 
                                     (nullable ? ", NULL" : ", NOT NULL") + ")");
                }
                
                // Contar registros existentes
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Usuarios")) {
                    if (rs.next()) {
                        System.out.println("✓ Total de usuarios en la tabla: " + rs.getInt("total"));
                    }
                }
                
                // Mostrar algunos usuarios si existen
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT ID, Nombre, Username, Mail FROM Usuarios")) {
                    System.out.println("Usuarios registrados:");
                    while (rs.next()) {
                        System.out.println("  - ID: " + rs.getInt("ID") + 
                                         ", Usuario: " + rs.getString("Username") + 
                                         ", Nombre: " + rs.getString("Nombre") + 
                                         ", Email: " + rs.getString("Mail"));
                    }
                }
                
            } else {
                System.out.println("✗ Tabla USUARIOS no encontrada");
                
                // Mostrar todas las tablas disponibles
                System.out.println("Tablas disponibles:");
                ResultSet allTables = metaData.getTables(null, null, null, new String[]{"TABLE"});
                while (allTables.next()) {
                    System.out.println("  - " + allTables.getString("TABLE_NAME"));
                }
            }
            
        } catch (SQLException e) {
            System.out.println("✗ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
