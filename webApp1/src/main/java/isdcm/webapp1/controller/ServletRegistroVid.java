package isdcm.webapp1.controller;

import isdcm.webapp1.dao.VideoDAO;
import isdcm.webapp1.model.Video;
import isdcm.webapp1.model.VideoMetadata;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ServletRegistroVid", urlPatterns = {"/videos/registro"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
        maxFileSize = 1024 * 1024 * 100,      // 100 MB
        maxRequestSize = 1024 * 1024 * 150    // 150 MB
)
public class ServletRegistroVid extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(ServletRegistroVid.class.getName());
    
    private final VideoDAO videoDAO = new VideoDAO();
    private static final String UPLOAD_DIRECTORY = "videos";
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Create upload directory if it doesn't exist
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            logger.log(Level.INFO, "Directorio de videos creado: {0}", uploadPath);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            logger.log(Level.WARNING, "Intento de acceso a registro de video sin autenticación");
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }
        
        logger.log(Level.INFO, "Mostrando formulario de registro de video para usuario: {0}", 
                session.getAttribute("usuario"));
        request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                logger.log(Level.WARNING, "Intento de subir video sin autenticación");
                response.sendRedirect(request.getContextPath() + "/usuarios");
                return;
            }
            
            // Get user information from session
            int userId = (Integer) session.getAttribute("userId");
            String username = (String) session.getAttribute("usuario");
            
            logger.log(Level.INFO, "Procesando subida de video para usuario: {0}", username);
            
            // Get form data
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            
            // Get uploaded file
            Part filePart = request.getPart("videoFile");
            
            // Validations
            if (titulo == null || titulo.trim().isEmpty()) {
                logger.log(Level.WARNING, "Intento de subir video sin título");
                request.setAttribute("error", "El título es obligatorio");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            if (filePart == null || filePart.getSize() == 0) {
                logger.log(Level.WARNING, "Intento de subir video sin archivo");
                request.setAttribute("error", "El archivo de video es obligatorio");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            // Check if the user has already uploaded a video with the same title
            List<Video> userVideos = videoDAO.findByAutor(userId);
            for (Video existingVideo : userVideos) {
                if (existingVideo.getTitulo().equalsIgnoreCase(titulo)) {
                    logger.log(Level.WARNING, "El usuario {0} intentó subir un video con título duplicado: {1}",
                            new Object[]{username, titulo});
                    request.setAttribute("error", "Ya has subido un video con este título. Por favor, utiliza un título diferente.");
                    request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                    return;
                }
            }
            
            logger.log(Level.INFO, "Iniciando procesamiento del video: {0}", titulo);
            
            // Extract metadata from video file entirely on the server side
            VideoMetadata metadata = extractVideoMetadata(filePart);
            
            // Process and save the video file
            String fileName = getSubmittedFileName(filePart);
            String fileExtension = getFileExtension(fileName);
            
            // Generate unique filename to avoid conflicts
            String uniqueFileName = username + "_" + UUID.randomUUID().toString() + "." + fileExtension;
            
            // Create the upload directory path
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            logger.log(Level.INFO, "Guardando video en: {0}", uploadPath + File.separator + uniqueFileName);
            
            // Save file to server
            Path filePath = Paths.get(uploadPath + File.separator + uniqueFileName);
            try (InputStream inputStream = filePart.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Generate URL for the video (relative to the application context)
            String videoUrl = request.getContextPath() + "/" + UPLOAD_DIRECTORY + "/" + uniqueFileName;
            
            // Create new video with the extracted metadata
            // Ahora estamos usando el constructor que solo acepta ID del autor
            Video video = new Video(
                    titulo,
                    userId,    // ID del usuario como autor
                    metadata.getCreationDate(), 
                    metadata.getDuration(),
                    descripcion, 
                    metadata.getFormat(), 
                    videoUrl
            );
            
            // Save to database
            videoDAO.insert(video);
            logger.log(Level.INFO, "Video registrado exitosamente: {0} por {1}", new Object[]{titulo, username});
            
            request.setAttribute("success", "Video registrado con éxito");
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error de base de datos al procesar el video", ex);
            request.setAttribute("error", "Error de base de datos: " + ex.getMessage());
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error general al procesar el video", ex);
            request.setAttribute("error", "Error al procesar el video: " + ex.getMessage());
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
        }
    }
    
    /**
     * Extracts all metadata from the video file
     * In a production environment, this would use a proper video processing library
     */
    private VideoMetadata extractVideoMetadata(Part filePart) {
        // Get current date for creation date
        Date creationDate = Date.valueOf(LocalDate.now());
        
        // Extract duration based on file size (simulated)
        Time duration = extractVideoDuration(filePart);
        
        // Extract format from filename
        String fileName = getSubmittedFileName(filePart);
        String format = getFileExtension(fileName);
        
        logger.log(Level.INFO, "Metadatos extraídos - Formato: {0}, Duración: {1}, Fecha: {2}", 
                new Object[]{format, duration, creationDate});
        
        return new VideoMetadata(creationDate, duration, format);
    }
    
    private String getSubmittedFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String content : contentDisposition.split(";")) {
                if (content.trim().startsWith("filename")) {
                    return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        }
        return "unknown";
    }
    
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "mp4"; // Default extension
    }
    
    private Time extractVideoDuration(Part filePart) {
        // In a real application, you'd use a library like Xuggler, ffmpeg, or MediaInfo
        // to extract the actual duration from the video file
        
        // Here we're simulating it based on file size
        // 1MB ~ 10 seconds (just a rough approximation)
        long fileSizeInMB = filePart.getSize() / (1024 * 1024);
        int seconds = (int) (fileSizeInMB * 10);
        
        // Ensure at least 5 seconds
        if (seconds < 5) seconds = 5;
        
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;
        
        return Time.valueOf(LocalTime.of(hours, minutes, remainingSeconds));
    }
}