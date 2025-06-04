package isdcm.webapp1.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filtro para redirigir todas las solicitudes de video a la página de registro
 * ya que se han eliminado todas las funcionalidades excepto el registro de usuarios.
 */
public class VideosURLFilter implements Filter {
    
    private static final Logger LOGGER = Logger.getLogger(VideosURLFilter.class.getName());    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        LOGGER.log(Level.INFO, "Interceptando solicitud a URL de video: {0}", request.getRequestURI());
        
        try {
            // Establecer un mensaje para mostrar al usuario como atributo de request
            // para evitar problemas de sesión
            request.setAttribute("error", 
                    "Funcionalidad de videos no disponible. Solo se permite el registro de usuarios.");
              // Redirigir a la página de registro usando sendRedirect (no forward)
            response.sendRedirect(request.getContextPath() + "/usuarios/registro");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al redirigir desde el filtro: {0}", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/usuarios/registro");
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se necesita inicialización especial
    }
    
    @Override
    public void destroy() {
        // No se necesitan operaciones de limpieza
    }
}
