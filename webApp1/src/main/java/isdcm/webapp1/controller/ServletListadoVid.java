package isdcm.webapp1.controller;

import isdcm.webapp1.dao.VideoDAO;
import isdcm.webapp1.model.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ServletListadoVid", urlPatterns = {"/videos/lista", "/videos/play/*", "/videos/mis-videos"})
public class ServletListadoVid extends HttpServlet {
    
    private final VideoDAO videoDAO = new VideoDAO();
    private static final Logger LOGGER = Logger.getLogger(ServletListadoVid.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }
        
        String uri = request.getRequestURI();
        
        try {
            if (uri.contains("/videos/play/")) {
                // Play specific video
                int videoId = Integer.parseInt(uri.substring(uri.lastIndexOf("/") + 1));
                playVideo(request, response, videoId);
            } else if (uri.contains("/videos/mis-videos")) {
                // List only user's videos
                listUserVideos(request, response);
            } else {
                // List all videos with optional filters
                listVideos(request, response);
            }
        } catch (SQLException ex) {
            request.setAttribute("error", "Error de base de datos: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/videos/lista");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This method would handle search form submissions
        doGet(request, response);
    }
    
    private void listVideos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        // Get all videos (filtering would be added here)
        List<Video> videos = videoDAO.findAll();
        
        // Set as request attribute
        request.setAttribute("videos", videos);
        request.setAttribute("misVideos", Boolean.FALSE);
        
        // Forward to JSP
        request.getRequestDispatcher("/views/listadoVid.jsp").forward(request, response);
    }
    
    private void listUserVideos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute("userId");
        
        // Get user's videos
        List<Video> videos = videoDAO.findByAutor(userId);
        
        // Set as request attribute
        request.setAttribute("videos", videos);
        request.setAttribute("misVideos", Boolean.TRUE);
        
        // Forward to JSP
        request.getRequestDispatcher("/views/listadoVid.jsp").forward(request, response);
    }
    
    private void playVideo(HttpServletRequest request, HttpServletResponse response, int videoId)
            throws ServletException, IOException, SQLException {
        
        // Get video by ID
        Video video = videoDAO.findById(videoId);
        
        if (video != null) {
            // Increment reproductions
            //videoDAO.incrementarReproduccion(videoId);
            try {
                URL url = new URL("http://localhost:8080/webApp1/rest/play/" + videoId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Accept", "application/json");
                
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    LOGGER.log(Level.SEVERE, "Error incrementing plays for video {0}. Response code: {1}", 
                            new Object[]{videoId, responseCode});
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error incrementing plays for video " + videoId, e);
            }
            
            // Get all videos for recommendations (excluding current video)
            List<Video> allVideos = videoDAO.findAll();
            List<Video> recommendations = allVideos.stream()
                    .filter(v -> v.getId() != videoId)
                    .limit(5)  // Limitamos a 5 recomendaciones
                    .toList();
            
            // Set video and recommendations as request attributes
            request.setAttribute("video", video);
            request.setAttribute("recommendations", recommendations);
            
            // Forward to JSP
            request.getRequestDispatcher("/views/playVideo.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/videos/lista");
        }
    }
}