package isdcm.webapp1.dao;

import isdcm.webapp1.model.Video;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoDAO {
    
    private static final Logger logger = Logger.getLogger(VideoDAO.class.getName());
    
    private Connection getConnection() throws SQLException {
        logger.log(Level.INFO, "Estableciendo conexión a la base de datos");
        // You'll need to adjust these connection parameters based on your setup
        return DriverManager.getConnection(
            "jdbc:derby://localhost:1527/pr2", "pr2", "pr2");
    }
    
    public void insert(Video video) throws SQLException {
        String sql = "INSERT INTO Videos (Titulo, Autor, Fecha_Creacion, Duracion, " +
                     "Descripcion, Formato, URL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        logger.log(Level.INFO, "Insertando video: {0}", video.getTitulo());
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, video.getTitulo());
            ps.setInt(2, video.getAutorId());
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
                    logger.log(Level.INFO, "Video insertado con ID: {0}", video.getId());
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar el video: {0}", e.getMessage());
            throw e;
        }
    }
    
    public void update(Video video) throws SQLException {
        String sql = "UPDATE Videos SET Titulo = ?, Autor = ?, Fecha_Creacion = ?, " +
                     "Duracion = ?, Reproducciones = ?, Descripcion = ?, Formato = ?, " +
                     "URL = ? WHERE ID = ?";
        
        logger.log(Level.INFO, "Actualizando video con ID: {0}", video.getId());
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, video.getTitulo());
            ps.setInt(2, video.getAutorId());
            ps.setDate(3, video.getFechaCreacion());
            ps.setTime(4, video.getDuracion());
            ps.setInt(5, video.getReproducciones());
            ps.setString(6, video.getDescripcion());
            ps.setString(7, video.getFormato());
            ps.setString(8, video.getUrl());
            ps.setInt(9, video.getId());
            
            int rowsAffected = ps.executeUpdate();
            logger.log(Level.INFO, "Video actualizado. Filas afectadas: {0}", rowsAffected);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el video: {0}", e.getMessage());
            throw e;
        }
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Videos WHERE ID = ?";
        
        logger.log(Level.INFO, "Eliminando video con ID: {0}", id);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            
            logger.log(Level.INFO, "Video eliminado. Filas afectadas: {0}", rowsAffected);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el video: {0}", e.getMessage());
            throw e;
        }
    }
    
    public Video findById(int id) throws SQLException {
        String sql = "SELECT v.*, u.Username as Autor_Nombre FROM Videos v " +
                    "JOIN Usuarios u ON v.Autor = u.ID " +
                    "WHERE v.ID = ?";
        
        logger.log(Level.INFO, "Buscando video con ID: {0}", id);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Video video = extractVideoFromResultSet(rs);
                    logger.log(Level.INFO, "Video encontrado: {0}", video.getTitulo());
                    return video;
                } else {
                    logger.log(Level.INFO, "No se encontró el video con ID: {0}", id);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar el video: {0}", e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    public List<Video> findAll() throws SQLException {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT v.*, u.Username as Autor_Nombre FROM Videos v " +
                    "JOIN Usuarios u ON v.Autor = u.ID";
        
        logger.log(Level.INFO, "Recuperando todos los videos");
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                videos.add(extractVideoFromResultSet(rs));
            }
            
            logger.log(Level.INFO, "Videos recuperados: {0}", videos.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al recuperar todos los videos: {0}", e.getMessage());
            throw e;
        }
        
        return videos;
    }
    
    public List<Video> findByAutor(int autorId) throws SQLException {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT v.*, u.Username as Autor_Nombre FROM Videos v " +
                    "JOIN Usuarios u ON v.Autor = u.ID " +
                    "WHERE v.Autor = ?";
        
        logger.log(Level.INFO, "Buscando videos del autor con ID: {0}", autorId);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, autorId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    videos.add(extractVideoFromResultSet(rs));
                }
            }
            
            logger.log(Level.INFO, "Videos encontrados para el autor {0}: {1}", new Object[]{autorId, videos.size()});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar videos por autor: {0}", e.getMessage());
            throw e;
        }
        
        return videos;
    }
    
    public void incrementarReproduccion(int id) throws SQLException {
        String sql = "UPDATE Videos SET Reproducciones = Reproducciones + 1 WHERE ID = ?";
        
        logger.log(Level.INFO, "Incrementando reproducciones para el video con ID: {0}", id);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Reproducción incrementada exitosamente");
            } else {
                logger.log(Level.WARNING, "No se pudo incrementar la reproducción. El video posiblemente no existe");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al incrementar reproducciones: {0}", e.getMessage());
            throw e;
        }
    }
    
    private Video extractVideoFromResultSet(ResultSet rs) throws SQLException {
        // Extraer el nombre del autor del JOIN
        String autorNombre = rs.getString("Autor_Nombre");
        
        return new Video(
            rs.getInt("ID"),
            rs.getString("Titulo"),
            autorNombre, // Nombre del autor obtenido del JOIN
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
