package co.edu.unbosque.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

public class SintomaConteo {
    @Field("idSintoma")
    private Long idSintoma;

    @Field("nombre")
    private String nombre;

    @Field("conteo")
    private Integer conteo;

    public SintomaConteo() {
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
