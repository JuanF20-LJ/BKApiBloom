package co.edu.unbosque.entity.mongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "articulos")
public class Articulo {
    @Id
    private String idArticulo;

    @Field("schemaVersion")
    private Integer schemaVersion;

    @Field("titulo")
    private String titulo;

    @Field("resumen")
    private String resumen;

    @Field("contenido")
    private String contenido;

    @Field("urlImagen")
    private String urlImagen;

    @Field("autor")
    private String autor;

    @Field("fechaPublicacion")
    private LocalDate fechaPublicacion;

    @Field("categoria")
    @Indexed
    private String categoria;

    @Field("fuente")
    private Fuente fuente;

    @Field("totalComentarios")
    private Integer totalComentarios;

    @Field("comentariosRecientes")
    private List<ComentarioReciente> comentariosRecientes = new ArrayList<ComentarioReciente>();

    public Articulo() {
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
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

    public Fuente getFuente() {
        return fuente;
    }

    public void setFuente(Fuente fuente) {
        this.fuente = fuente;
    }

    public Integer getTotalComentarios() {
        return totalComentarios;
    }

    public void setTotalComentarios(Integer totalComentarios) {
        this.totalComentarios = totalComentarios;
    }

    public List<ComentarioReciente> getComentariosRecientes() {
        return comentariosRecientes;
    }

    public void setComentariosRecientes(List<ComentarioReciente> comentariosRecientes) {
        this.comentariosRecientes = comentariosRecientes;
    }
}
