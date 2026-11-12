package co.edu.unbosque.dto;

public class ComentarioCreacionDTO {
    private Long idUsuario;

    private String contenido;

    public ComentarioCreacionDTO() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
