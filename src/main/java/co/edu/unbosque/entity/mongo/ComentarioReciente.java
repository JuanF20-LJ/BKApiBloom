package co.edu.unbosque.entity.mongo;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Field;

public class ComentarioReciente {
    @Field("idComentario")
    private String idComentario;

    @Field("autora")
    private AutoraComentario autora;

    @Field("contenido")
    private String contenido;

    @Field("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Field("fechaEdicion")
    private LocalDateTime fechaEdicion;

    public ComentarioReciente() {
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
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
