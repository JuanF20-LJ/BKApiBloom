package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "actividad_sexual")
public class ActividadSexual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idactividadsexual")
    private Long idActividadSexual;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    public ActividadSexual() {
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
