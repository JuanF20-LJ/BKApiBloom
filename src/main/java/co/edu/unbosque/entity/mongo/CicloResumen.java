package co.edu.unbosque.entity.mongo;

import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Field;

public class CicloResumen {
    @Field("idCicloMenstrual")
    private Long idCicloMenstrual;

    @Field("fechaInicio")
    private LocalDate fechaInicio;

    @Field("duracion")
    private Integer duracion;

    public CicloResumen() {
    }

    public Long getIdCicloMenstrual() {
        return idCicloMenstrual;
    }

    public void setIdCicloMenstrual(Long idCicloMenstrual) {
        this.idCicloMenstrual = idCicloMenstrual;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}
