package isdcm.webapp1.model;

import java.io.Serializable;

/**
 * Model class representing a user in the system.
 * Maps to the Usuarios table in the database.
 */
public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private String mail;
    private String username;
    private String pass;

    // Default constructor
    public Usuario() {
    }

    // Constructor with all fields except ID (which is auto-generated)
    public Usuario(String nombre, String apellidos, String mail, String username, String pass) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.mail = mail;
        this.username = username;
        this.pass = pass;
    }

    // Constructor with all fields
    public Usuario(int id, String nombre, String apellidos, String mail, String username, String pass) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.mail = mail;
        this.username = username;
        this.pass = pass;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", pass='********'" +
                '}';
    }
}