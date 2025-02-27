package isdcm.webapp1.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

/**
 * Model class representing a video in the system.
 * Maps to the Videos table in the database.
 */
public class Video implements Serializable {
    private int id;
    private String titulo;
    private int autor;
    private Date fechaCreacion;
    private Time duracion;
    private int reproducciones;
    private String descripcion;
    private String formato;
    private String url;

    // Default constructor
    public Video() {
        this.reproducciones = 0;
    }

    // Constructor with all fields except ID and reproducciones (which has default value)
    public Video(String titulo, int autor, Date fechaCreacion, Time duracion, 
                String descripcion, String formato, String url) {
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.reproducciones = 0;
        this.descripcion = descripcion;
        this.formato = formato;
        this.url = url;
    }

    // Constructor with all fields
    public Video(int id, String titulo, int autor, Date fechaCreacion, Time duracion,
                int reproducciones, String descripcion, String formato, String url) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.reproducciones = reproducciones;
        this.descripcion = descripcion;
        this.formato = formato;
        this.url = url;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAutor() {
        return autor;
    }

    public void setAutor(int autor) {
        this.autor = autor;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Time getDuracion() {
        return duracion;
    }

    public void setDuracion(Time duracion) {
        this.duracion = duracion;
    }

    public int getReproducciones() {
        return reproducciones;
    }

    public void setReproducciones(int reproducciones) {
        this.reproducciones = reproducciones;
    }

    public void incrementarReproducciones() {
        this.reproducciones++;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", fechaCreacion=" + fechaCreacion +
                ", duracion=" + duracion +
                ", reproducciones=" + reproducciones +
                ", descripcion='" + descripcion + '\'' +
                ", formato='" + formato + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}