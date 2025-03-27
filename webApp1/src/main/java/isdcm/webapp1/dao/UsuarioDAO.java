package isdcm.webapp1.dao;

import isdcm.webapp1.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    private Connection getConnection() throws SQLException {
        // You'll need to adjust these connection parameters based on your setup
        return DriverManager.getConnection(
            "jdbc:derby://localhost:1527/pr2", "pr2", "pr2");
    }
    
    public void insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nombre, Apellidos, Mail, Username, Pass) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getUsername());
            ps.setString(5, usuario.getPass());
            
            ps.executeUpdate();
            
            // Get generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
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