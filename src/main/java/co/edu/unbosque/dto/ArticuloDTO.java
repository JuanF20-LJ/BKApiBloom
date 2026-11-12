package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDTO {
    private String idArticulo;

    private String titulo;

    private String resumen;

    private String contenido;

    private String urlImagen;

    private String autor;

    private LocalDate fechaPublicacion;

    private String categoria;

    private FuenteDTO fuente;

    private Integer totalComentarios;

    private List<ComentarioDTO> comentariosRecientes = new ArrayList<ComentarioDTO>();

    public ArticuloDTO() {
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public FuenteDTO getFuente() {
        return fuente;
    }

    public void setFuente(FuenteDTO fuente) {
        this.fuente = fuente;
    }

    public Integer getTotalComentarios() {
        return totalComentarios;
    }

    public void setTotalComentarios(Integer totalComentarios) {
        this.totalComentarios = totalComentarios;
    }

    public List<ComentarioDTO> getComentariosRecientes() {
        return comentariosRecientes;
    }

    public void setComentariosRecientes(List<ComentarioDTO> comentariosRecientes) {
        this.comentariosRecientes = comentariosRecientes;
    }
}
