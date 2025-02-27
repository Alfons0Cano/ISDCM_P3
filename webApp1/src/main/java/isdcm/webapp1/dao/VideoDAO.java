package isdcm.webapp1.dao;

import isdcm.webapp1.model.Video;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {
    
    private Connection getConnection() throws SQLException {
        // You'll need to adjust these connection parameters based on your setup
        return DriverManager.getConnection(
            "jdbc:derby://localhost:1527/pr2", "pr2", "pr2");
    }
    
    public void insert(Video video) throws SQLException {
        String sql = "INSERT INTO Videos (Titulo, Autor, Fecha_Creacion, Duracion, " +
                     "Descripcion, Formato, URL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, video.getTitulo());
            ps.setInt(2, video.getAutor());
            ps.setDate(3, video.getFechaCreacion());
            ps.setTime(4, video.getDuracion());
            ps.setString(5, video.getDescripcion());
            ps.setString(6, video.getFormato());
            ps.setString(7, video.getUrl());
            
            ps.executeUpdate();
            
            // Get generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    video.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public void update(Video video) throws SQLException {
        String sql = "UPDATE Videos SET Titulo = ?, Autor = ?, Fecha_Creacion = ?, " +
                     "Duracion = ?, Reproducciones = ?, Descripcion = ?, Formato = ?, " +
                     "URL = ? WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, video.getTitulo());
            ps.setInt(2, video.getAutor());
            ps.setDate(3, video.getFechaCreacion());
            ps.setTime(4, video.getDuracion());
            ps.setInt(5, video.getReproducciones());
            ps.setString(6, video.getDescripcion());
            ps.setString(7, video.getFormato());
            ps.setString(8, video.getUrl());
            ps.setInt(9, video.getId());
            
            ps.executeUpdate();
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Videos WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public Video findById(int id) throws SQLException {
        String sql = "SELECT * FROM Videos WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractVideoFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    public List<Video> findAll() throws SQLException {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM Videos";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(extractVideoFromResultSet(rs));
            }
        }
        
        return videos;
    }
    
    public List<Video> findByAutor(int autorId) throws SQLException {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM Videos WHERE Autor = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, autorId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    videos.add(extractVideoFromResultSet(rs));
                }
            }
        }
        
        return videos;
    }
    
    public void incrementarReproduccion(int id) throws SQLException {
        String sql = "UPDATE Videos SET Reproducciones = Reproducciones + 1 WHERE ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    private Video extractVideoFromResultSet(ResultSet rs) throws SQLException {
        return new Video(
            rs.getInt("ID"),
            rs.getString("Titulo"),
            rs.getInt("Autor"),
            rs.getDate("Fecha_Creacion"),
            rs.getTime("Duracion"),
            rs.getInt("Reproducciones"),
            rs.getString("Descripcion"),
            rs.getString("Formato"),
            rs.getString("URL")
        );
    }
}
