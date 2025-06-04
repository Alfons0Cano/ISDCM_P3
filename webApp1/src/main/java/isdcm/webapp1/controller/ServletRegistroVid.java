package isdcm.webapp1.controller;

import isdcm.webapp1.dao.VideoDAO;
import isdcm.webapp1.model.Usuario;
import isdcm.webapp1.model.Video;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String uploadPath;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Obtener la ruta real del directorio de despliegue
        uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (created) {
                logger.log(Level.INFO, "Directorio de videos creado exitosamente en: {0}", uploadPath);
            } else {
                logger.log(Level.SEVERE, "No se pudo crear el directorio de videos en: {0}", uploadPath);
                throw new ServletException("No se pudo crear el directorio de videos");
            }
        }
        logger.log(Level.INFO, "Usando directorio de videos: {0}", uploadPath);
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
            
            // Usar la ruta completa almacenada durante la inicialización
            String fullFilePath = uploadPath + File.separator + uniqueFileName;
            logger.log(Level.INFO, "Guardando video en: {0}", fullFilePath);
            
            // Asegurarse de que el directorio existe antes de guardar
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new IOException("No se pudo crear el directorio para guardar el video");
                }
            }
            
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
              // Actualizar la URL del video para que use la ruta relativa correcta
            String videoUrl = request.getContextPath() + "/" + UPLOAD_DIRECTORY + "/" + uniqueFileName;
            
            // Create new video object - asegurando que se use el constructor correcto para todos los campos
            Video video = new Video();
            video.setTitulo(titulo);
            video.setAutorId(userId);
            video.setAutor(username); // Añadimos explícitamente el nombre de usuario
            video.setFechaCreacion(creationDate);
            video.setDuracion(duration);
            video.setDescripcion(descripcion);
            video.setFormato(fileExtension);
            video.setUrl(videoUrl);
            video.setReproducciones(0);
            
            // En lugar de guardar directamente en la base de datos, usamos el servicio REST de webApp2
            try {                // Convertir el objeto Video a JSON manualmente (podríamos usar una biblioteca JSON)
                // Formatear fecha y duración correctamente
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String fechaCreacionStr = dateFormat.format(video.getFechaCreacion());
                String duracionStr = video.getDuracion().toString();
                  // Construimos el JSON exactamente como lo espera webApp2
                String jsonVideo = String.format(
                    "{\"titulo\":\"%s\",\"autor\":\"%s\",\"autorId\":%d,\"fechaCreacion\":\"%s\",\"duracion\":\"%s\",\"descripcion\":\"%s\",\"formato\":\"%s\",\"url\":\"%s\",\"reproducciones\":%d}",
                    video.getTitulo(), 
                    video.getAutor(), 
                    video.getAutorId(),
                    fechaCreacionStr, 
                    duracionStr, 
                    video.getDescripcion() != null ? video.getDescripcion().replace("\"", "\\\"") : "", 
                    video.getFormato(), 
                    video.getUrl(),
                    video.getReproducciones()
                );
                
                logger.log(Level.INFO, "Enviando datos del video al servicio REST: {0}", jsonVideo);
                
                // Crear conexión HTTP al servlet REST de la misma aplicación
                String restEndpoint = request.getContextPath() + "/rest/videos";
                URL url = new URL(request.getScheme(), request.getServerName(), 
                                 request.getServerPort(), restEndpoint);
                
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                
                // Enviar datos JSON
                try (java.io.OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonVideo.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                
                // Leer respuesta
                int respCode = conn.getResponseCode();
                
                if (respCode >= 200 && respCode < 300) {
                    // Éxito
                    logger.log(Level.INFO, "Video registrado exitosamente a través del servicio REST");
                    request.setAttribute("success", "Video registrado con éxito");
                } else {
                    // Error
                    StringBuilder errorResponse = new StringBuilder();
                    try (java.io.BufferedReader br = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            errorResponse.append(line);
                        }
                    }
                    
                    logger.log(Level.SEVERE, "Error al registrar video a través del servicio REST: {0}", errorResponse.toString());
                    request.setAttribute("error", "Error al registrar video: " + errorResponse.toString());
                }
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al comunicarse con el servicio REST", e);
                request.setAttribute("error", "Error al comunicarse con el servicio de videos: " + e.getMessage());
            }
            
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