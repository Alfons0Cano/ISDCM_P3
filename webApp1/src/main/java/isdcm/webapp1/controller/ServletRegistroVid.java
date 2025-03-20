package isdcm.webapp1.controller;

import isdcm.webapp1.dao.VideoDAO;
import isdcm.webapp1.model.Usuario;
import isdcm.webapp1.model.Video;
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
        
        // En lugar de convertir directamente, simplemente usamos toString() para el registro
        logger.log(Level.INFO, "Mostrando formulario de registro de video para usuario: {0}", 
                session.getAttribute("usuario").toString());
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
            
            // Get user information from session - ahora manejando el usuario como objeto
            Usuario usuarioObj = (Usuario) session.getAttribute("usuario");
            int userId = usuarioObj.getId();
            String username = usuarioObj.getUsername();
            
            logger.log(Level.INFO, "Procesando subida de video para usuario: {0}", username);
            
            // Get form data
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String videoDurationStr = request.getParameter("videoDuration");
            
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
            
            // Process and save the video file first to get accurate metadata
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
            
            // Full path to the saved file
            String fullFilePath = uploadPath + File.separator + uniqueFileName;
            logger.log(Level.INFO, "Guardando video en: {0}", fullFilePath);
            
            // Save file to server
            Path filePath = Paths.get(fullFilePath);
            try (InputStream inputStream = filePart.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Now that the file is saved, extract metadata
            Date creationDate = new Date(new File(fullFilePath).lastModified());
            
            // Convert duration from seconds to Time
            int durationSeconds = Integer.parseInt(videoDurationStr);
            int hours = durationSeconds / 3600;
            int minutes = (durationSeconds % 3600) / 60;
            int seconds = durationSeconds % 60;
            Time duration = Time.valueOf(LocalTime.of(hours, minutes, seconds));
            
            // Generate URL for the video (relative to the application context)
            String videoUrl = request.getContextPath() + "/" + UPLOAD_DIRECTORY + "/" + uniqueFileName;
            
            // Create new video object
            Video video = new Video(
                    titulo,
                    userId,
                    creationDate,
                    duration,
                    descripcion,
                    fileExtension,
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
}