package co.edu.unbosque.dto;

public class SintomaConteoDTO {
    private Long idSintoma;

    private String nombre;

    private Integer conteo;

    public SintomaConteoDTO() {
    }

    public Long getIdSintoma() {
        return idSintoma;
    }

    public void setIdSintoma(Long idSintoma) {
        this.idSintoma = idSintoma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getConteo() {
        return conteo;
    }

    public void setConteo(Integer conteo) {
        this.conteo = conteo;
    }
}
