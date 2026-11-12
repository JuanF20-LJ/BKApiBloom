package co.edu.unbosque.dto;

import java.time.LocalDate;

public class MenstruacionDTO {
    private Long idMenstruacion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer duracion;

    private String estado;

    private Long idCicloMenstrual;

    public MenstruacionDTO() {
    }

    public Long getIdMenstruacion() {
        return idMenstruacion;
    }

    public void setIdMenstruacion(Long idMenstruacion) {
        this.idMenstruacion = idMenstruacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdCicloMenstrual() {
        return idCicloMenstrual;
    }

    public void setIdCicloMenstrual(Long idCicloMenstrual) {
        this.idCicloMenstrual = idCicloMenstrual;
    }
}
