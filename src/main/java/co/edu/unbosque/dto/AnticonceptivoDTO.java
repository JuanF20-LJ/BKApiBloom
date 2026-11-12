package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnticonceptivoDTO {
    private Long idAnticonceptivo;

    private String nombre;

    private String estado;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private LocalDate proximaAplicacion;

    private Integer frecuenciaDias;

    private String observaciones;

    private LocalDateTime fechaRegistro;

    public AnticonceptivoDTO() {
    }

    public Long getIdAnticonceptivo() {
        return idAnticonceptivo;
    }

    public void setIdAnticonceptivo(Long idAnticonceptivo) {
        this.idAnticonceptivo = idAnticonceptivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public LocalDate getProximaAplicacion() {
        return proximaAplicacion;
    }

    public void setProximaAplicacion(LocalDate proximaAplicacion) {
        this.proximaAplicacion = proximaAplicacion;
    }

    public Integer getFrecuenciaDias() {
        return frecuenciaDias;
    }

    public void setFrecuenciaDias(Integer frecuenciaDias) {
        this.frecuenciaDias = frecuenciaDias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
