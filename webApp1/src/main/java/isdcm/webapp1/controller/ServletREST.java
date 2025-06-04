package isdcm.webapp1.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
    name = "ServletREST",
    urlPatterns = {"/rest/search", "/rest/play/*", "/rest/videos"},
    loadOnStartup = 1
)
public class ServletREST extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ServletREST.class.getName());
    private static final long serialVersionUID = 1L;
    private static final String BASE_URL = "http://localhost:42435/webApp2/resources/rest";
    private static final int TIMEOUT = 5000;

    @Override
    public void init() throws ServletException {
        LOGGER.info("ServletREST initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener parámetros de búsqueda
            String titulo = request.getParameter("titulo");
            String autor = request.getParameter("autor");
            String fecha = request.getParameter("fecha");
            
            LOGGER.log(Level.INFO, "Parámetros recibidos - titulo: {0}, autor: {1}, fecha: {2}", 
                    new Object[]{titulo, autor, fecha});
            
            // Construir URL del servicio externo
            StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/search?");
            if (titulo != null && !titulo.isEmpty()) {
                urlBuilder.append("titulo=").append(java.net.URLEncoder.encode(titulo, "UTF-8"));
            }
            if (autor != null && !autor.isEmpty()) {
                if (urlBuilder.toString().contains("=")) {
                    urlBuilder.append("&");
                }
                urlBuilder.append("autor=").append(java.net.URLEncoder.encode(autor, "UTF-8"));
            }
            if (fecha != null && !fecha.isEmpty()) {
                if (urlBuilder.toString().contains("=")) {
                    urlBuilder.append("&");
                }
                urlBuilder.append("fecha=").append(java.net.URLEncoder.encode(fecha, "UTF-8"));
            }
            
            String finalUrl = urlBuilder.toString();
            LOGGER.log(Level.INFO, "URL construida: {0}", finalUrl);
            
            // Crear conexión al servicio externo
            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            // Obtener respuesta
            int responseCode = conn.getResponseCode();
            LOGGER.log(Level.INFO, "Código de respuesta del servicio externo: {0}", responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();
                
                String jsonResponse = responseContent.toString();
                LOGGER.log(Level.INFO, "Respuesta recibida: {0}", jsonResponse);
                
                // Configurar respuesta
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.getWriter().write(jsonResponse);
            } else {
                String errorMessage = "Error al conectar con el servicio externo";
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorContent = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorContent.append(errorLine);
                    }
                    if (errorContent.length() > 0) {
                        errorMessage = errorContent.toString();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al leer el stream de error", e);
                }
                
                LOGGER.log(Level.SEVERE, "Error en la respuesta: {0}", errorMessage);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en el servlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || !pathInfo.startsWith("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid video ID\"}");
                return;
            }

            String videoId = pathInfo.substring(1);
            String playUrl = BASE_URL + "/play/" + videoId;
            
            LOGGER.log(Level.INFO, "Incrementing plays for video: {0}", videoId);
            
            HttpURLConnection conn = (HttpURLConnection) new URL(playUrl).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            
            int responseCode = conn.getResponseCode();
            LOGGER.log(Level.INFO, "Play increment response code: {0}", responseCode);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String jsonResponse = in.lines().collect(Collectors.joining());
                    LOGGER.log(Level.INFO, "Play increment response: {0}", jsonResponse);
                    response.getWriter().write(jsonResponse);
                }
            } else {
                String errorMessage = "Error al incrementar reproducciones";
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    errorMessage = errorReader.lines().collect(Collectors.joining());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al leer el stream de error", e);
                }
                
                LOGGER.log(Level.SEVERE, "Error en la respuesta: {0}", errorMessage);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en el servlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"error\":\"%s\"}", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Leer el cuerpo JSON de la solicitud
            StringBuilder buffer = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            
            String videoJsonData = buffer.toString();
            LOGGER.log(Level.INFO, "Recibida solicitud POST para crear video: {0}", videoJsonData);
            
            // Construir URL para el servicio REST de webApp2
            String createVideoUrl = BASE_URL + "/videos";
            
            // Crear conexión HTTP
            HttpURLConnection conn = (HttpURLConnection) new URL(createVideoUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            
            // Enviar los datos JSON al servicio REST
            try (java.io.OutputStream os = conn.getOutputStream()) {
                byte[] input = videoJsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Obtener respuesta
            int responseCode = conn.getResponseCode();
            LOGGER.log(Level.INFO, "Código de respuesta del servicio REST para crear video: {0}", responseCode);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            
            // Procesar respuesta
            if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                // Respuesta exitosa (códigos 200-299)
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String jsonResponse = in.lines().collect(Collectors.joining());
                    LOGGER.log(Level.INFO, "Video creado correctamente: {0}", jsonResponse);
                    response.setStatus(responseCode); // Usar el mismo código que devolvió webApp2
                    response.getWriter().write(jsonResponse);
                }
            } else {
                // Error en la respuesta
                String errorMessage = "Error al crear el video";
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    errorMessage = errorReader.lines().collect(Collectors.joining());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al leer el stream de error", e);
                }
                
                LOGGER.log(Level.SEVERE, "Error al crear el video: {0}", errorMessage);
                response.setStatus(responseCode);
                response.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en el servlet POST", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"error\":\"%s\"}", e.getMessage()));
        }
    }
}