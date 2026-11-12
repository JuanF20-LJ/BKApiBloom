package co.edu.unbosque.dto;

public class ActividadSexualDTO {
    private Long idActividadSexual;

    private String nombre;

    public ActividadSexualDTO() {
    }

    public Long getIdActividadSexual() {
        return idActividadSexual;
    }

    public void setIdActividadSexual(Long idActividadSexual) {
        this.idActividadSexual = idActividadSexual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
