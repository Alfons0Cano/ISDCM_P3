package isdcm.webapp1.dao;

import isdcm.webapp1.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    static {
        try {
            // Registrar el driver de Derby explícitamente
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            System.out.println("DEBUG - Driver Derby registrado exitosamente");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR - No se pudo cargar el driver Derby: " + e.getMessage());
        }
    }
    
      private Connection getConnection() throws SQLException {
        // You'll need to adjust these connection parameters based on your setup
        String url = "jdbc:derby://localhost:1527/pr2";
        String user = "pr2";
        String password = "pr2";
        
        System.out.println("DEBUG - Intentando conectar a: " + url + " con usuario: " + user);
        
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("DEBUG - Conexión exitosa");
            return conn;
        } catch (SQLException e) {
            System.err.println("ERROR - Error de conexión: " + e.getMessage());
            throw e;
        }
    }
      public void insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nombre, Apellidos, Mail, Username, Pass) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        System.out.println("DEBUG - Ejecutando INSERT con SQL: " + sql);
        System.out.println("DEBUG - Valores: " + usuario.getNombre() + ", " + 
                          usuario.getApellidos() + ", " + usuario.getMail() + ", " + 
                          usuario.getUsername() + ", [PASSWORD]");
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getUsername());
            ps.setString(5, usuario.getPass());
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("DEBUG - Filas afectadas: " + rowsAffected);
            
            // Get generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    usuario.setId(generatedId);
                    System.out.println("DEBUG - ID generado: " + generatedId);
                } else {
                    System.out.println("DEBUG - No se generó ningún ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR - Error en insert(): " + e.getMessage());
            throw e;
        }
    }
    
    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuarios SET Nombre = ?, Apellidos = ?, Mail = ?, " +
                     "Username = ?, Pass = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getUsername());
            ps.setString(5, usuario.getPass());
            ps.setInt(6, usuario.getId());
            
            ps.executeUpdate();
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUsuarioFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE Username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUsuarioFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(extractUsuarioFromResultSet(rs));
            }
        }
        
        return usuarios;
    }
    
    public boolean validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE Username = ? AND Pass = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if credentials are valid
            }
        }
    }
    
    public boolean checkUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    public boolean checkMailExists(String mail) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Mail = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mail);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    private Usuario extractUsuarioFromResultSet(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("ID"),
            rs.getString("Nombre"),
            rs.getString("Apellidos"),
            rs.getString("Mail"),
            rs.getString("Username"),
            rs.getString("Pass")
        );
    }
}