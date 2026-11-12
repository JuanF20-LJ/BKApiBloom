package co.edu.unbosque.entity.mongo;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comentarios")
public class Comentario {
    @Id
    private String idComentario;

    @Field("schemaVersion")
    private Integer schemaVersion;

    @Field("idArticulo")
    @Indexed
    private String idArticulo;

    @Field("autora")
    private AutoraComentario autora;

    @Field("contenido")
    private String contenido;

    @Field("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Field("fechaEdicion")
    private LocalDateTime fechaEdicion;

    public Comentario() {
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public AutoraComentario getAutora() {
        return autora;
    }

    public void setAutora(AutoraComentario autora) {
        this.autora = autora;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(LocalDateTime fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }
}
