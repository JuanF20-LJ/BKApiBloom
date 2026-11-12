package co.edu.unbosque.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

public class Fuente {
    @Field("nombre")
    private String nombre;

    @Field("url")
    private String url;

    public Fuente() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
