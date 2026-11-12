package co.edu.unbosque.dto;

import java.time.LocalDate;

public class CicloMenstrualDTO {
    private Long idCicloMenstrual;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer duracion;

    private String estado;

    private String origen;

    private MenstruacionDTO menstruacion;

    public CicloMenstrualDTO() {
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public MenstruacionDTO getMenstruacion() {
        return menstruacion;
    }

    public void setMenstruacion(MenstruacionDTO menstruacion) {
        this.menstruacion = menstruacion;
    }
}
