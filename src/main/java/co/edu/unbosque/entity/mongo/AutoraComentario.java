package co.edu.unbosque.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

public class AutoraComentario {
    @Field("idUsuario")
    private Long idUsuario;

    @Field("apodo")
    private String apodo;

    public AutoraComentario() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }
}
