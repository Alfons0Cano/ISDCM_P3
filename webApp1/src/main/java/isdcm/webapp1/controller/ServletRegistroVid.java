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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "ServletRegistroVid", urlPatterns = {"/videos/registro"})
public class ServletRegistroVid extends HttpServlet {
    
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
        
        request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/usuarios");
                return;
            }
            
            // Get user ID from session
            int userId = (Integer) session.getAttribute("userId");
            
            // Get form data
            String titulo = request.getParameter("titulo");
            String fechaStr = request.getParameter("fecha");
            String duracionStr = request.getParameter("duracion");
            String descripcion = request.getParameter("descripcion");
            String formato = request.getParameter("formato");
            String url = request.getParameter("url");
            
            // Validations
            if (titulo == null || titulo.trim().isEmpty() ||
                fechaStr == null || fechaStr.trim().isEmpty() ||
                duracionStr == null || duracionStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Los campos título, fecha y duración son obligatorios");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            // Parse fecha (expected format: yyyy-MM-dd)
            Date fecha;
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                fecha = new Date(utilDate.getTime());
            } catch (ParseException e) {
                request.setAttribute("error", "Formato de fecha inválido. Use yyyy-MM-dd");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            // Parse duración (expected format: HH:mm:ss)
            Time duracion;
            try {
                java.util.Date utilTime = new SimpleDateFormat("HH:mm:ss").parse(duracionStr);
                duracion = new Time(utilTime.getTime());
            } catch (ParseException e) {
                request.setAttribute("error", "Formato de duración inválido. Use HH:mm:ss");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            // Create new video
            Video video = new Video(titulo, userId, fecha, duracion, descripcion, formato, url);
            
            // Save to database
            videoDAO.insert(video);
            
            request.setAttribute("message", "Video registrado con éxito");
            response.sendRedirect(request.getContextPath() + "/videos/lista");
            
        } catch (SQLException ex) {
            request.setAttribute("error", "Error de base de datos: " + ex.getMessage());
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
        }
    }
}