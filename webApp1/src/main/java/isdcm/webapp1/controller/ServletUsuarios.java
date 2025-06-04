package isdcm.webapp1.controller;

import isdcm.webapp1.dao.UsuarioDAO;
import isdcm.webapp1.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ServletUsuarios", urlPatterns = {"/usuarios/*"})
public class ServletUsuarios extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }
        
        switch (action) {
            case "/logout":
                logout(request, response);
                break;
            case "/registro":
                showRegistroForm(request, response);
                break;
            default:
                showLoginForm(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }
        
        try {
            switch (action) {
                case "/login":
                    login(request, response);
                    break;
                case "/registro":
                    registro(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/usuarios");
                    break;
            }        } catch (SQLException ex) {
            request.setAttribute("error", "Error de base de datos: " + ex.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            // Redirigir a la página de registro en lugar de a una que ya no existe
            session.setAttribute("message", "Ya has iniciado sesión. Solo está disponible el módulo de registro de usuarios.");
            response.sendRedirect(request.getContextPath() + "/usuarios/registro");
            return;
        }
        
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (usuarioDAO.validateLogin(username, password)) {
            Usuario usuario = usuarioDAO.findByUsername(username);
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("userId", usuario.getId());
            session.setAttribute("username", usuario.getUsername());
            
            // Mostrar mensaje de éxito en lugar de redirigir a una página que ya no existe
            session.setAttribute("message", "Inicio de sesión exitoso. Solo está disponible el módulo de registro de usuarios.");
            response.sendRedirect(request.getContextPath() + "/usuarios/registro");
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
      private void showRegistroForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Si hay un mensaje en la sesión, se recuperará automáticamente en la JSP
        request.getRequestDispatcher("/views/registroUsu.jsp").forward(request, response);
    }
      private void registro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String mail = request.getParameter("mail");
        String username = request.getParameter("username");
        String pass = request.getParameter("password");
        
        // Debug: imprimir parámetros recibidos
        System.out.println("DEBUG - Parámetros recibidos:");
        System.out.println("  nombre: " + nombre);
        System.out.println("  apellidos: " + apellidos);
        System.out.println("  mail: " + mail);
        System.out.println("  username: " + username);
        System.out.println("  password: " + (pass != null ? "[RECIBIDA]" : "[NULL]"));
        
        // Validations
        if (nombre == null || nombre.trim().isEmpty() ||
            apellidos == null || apellidos.trim().isEmpty() ||
            mail == null || mail.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            pass == null || pass.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos son obligatorios");
            request.getRequestDispatcher("/views/registroUsu.jsp").forward(request, response);
            return;
        }

        try {
            // Check if mail already exists
            if (usuarioDAO.checkMailExists(mail)) {
                request.setAttribute("error", "El mail del usuario ya existe");
                request.getRequestDispatcher("/views/registroUsu.jsp").forward(request, response);
                return;
            }
            
            // Check if username already exists
            if (usuarioDAO.checkUsernameExists(username)) {
                request.setAttribute("error", "El nombre de usuario ya existe");
                request.getRequestDispatcher("/views/registroUsu.jsp").forward(request, response);
                return;
            }
            
            // Create new user
            Usuario usuario = new Usuario(nombre, apellidos, mail, username, pass);
            System.out.println("DEBUG - Intentando insertar usuario: " + username);
            usuarioDAO.insert(usuario);
            System.out.println("DEBUG - Usuario insertado exitosamente con ID: " + usuario.getId());
            
            request.setAttribute("message", "Usuario registrado con éxito");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            
        } catch (SQLException e) {
            System.err.println("ERROR - Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            request.getRequestDispatcher("/views/registroUsu.jsp").forward(request, response);
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}