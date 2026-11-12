package co.edu.unbosque.dto;

import java.time.LocalDate;

public class CicloResumenDTO {
    private Long idCicloMenstrual;

    private LocalDate fechaInicio;

    private Integer duracion;

    public CicloResumenDTO() {
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
