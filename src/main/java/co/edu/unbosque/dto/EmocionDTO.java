package co.edu.unbosque.dto;

public class EmocionDTO {
    private Long idEmocion;

    private String nombre;

    private String categoria;

    public EmocionDTO() {
    }

    public Long getIdEmocion() {
        return idEmocion;
    }

    public void setIdEmocion(Long idEmocion) {
        this.idEmocion = idEmocion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
