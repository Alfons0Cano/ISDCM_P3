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
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ServletListadoVid", urlPatterns = {"/videos/lista", "/videos/play/*", "/videos/mis-videos"})
public class ServletListadoVid extends HttpServlet {
    
    private final VideoDAO videoDAO = new VideoDAO();
    
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
        
        // Get search parameters
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String fecha = request.getParameter("fecha");
        
        // Get all videos (filtering would be added here)
        List<Video> videos = videoDAO.findAll();
        
        // Set as request attribute
        request.setAttribute("videos", videos);
        
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
        request.setAttribute("misVideos", true);
        
        // Forward to JSP
        request.getRequestDispatcher("/views/listadoVid.jsp").forward(request, response);
    }
    
    private void playVideo(HttpServletRequest request, HttpServletResponse response, int videoId)
            throws ServletException, IOException, SQLException {
        
        // Get video by ID
        Video video = videoDAO.findById(videoId);
        
        if (video != null) {
            // Increment reproductions
            videoDAO.incrementarReproduccion(videoId);
            
            // Set video as request attribute
            request.setAttribute("video", video);
            
            // Forward to JSP
            request.getRequestDispatcher("/views/playVideo.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/videos/lista");
        }
    }
}