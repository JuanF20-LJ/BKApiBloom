package co.edu.unbosque.dto;

import java.time.LocalDate;

public class CicloActualDTO {
    private Long idCicloMenstrual;

    private LocalDate fechaInicio;

    private Integer diaDelCiclo;

    private Integer duracionEstimada;

    private Integer duracionEstimadaMenstruacion;

    private LocalDate fechaEstimadaSiguientePeriodo;

    private Integer diasParaSiguientePeriodo;

    private Boolean periodoRetrasado;

    private Integer diasRetraso;

    private LocalDate fechaEstimadaOvulacion;

    private String fase;

    private String metodoCalculo;

    public CicloActualDTO() {
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

    public Integer getDiaDelCiclo() {
        return diaDelCiclo;
    }

    public void setDiaDelCiclo(Integer diaDelCiclo) {
        this.diaDelCiclo = diaDelCiclo;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public Integer getDuracionEstimadaMenstruacion() {
        return duracionEstimadaMenstruacion;
    }

    public void setDuracionEstimadaMenstruacion(Integer duracionEstimadaMenstruacion) {
        this.duracionEstimadaMenstruacion = duracionEstimadaMenstruacion;
    }

    public LocalDate getFechaEstimadaSiguientePeriodo() {
        return fechaEstimadaSiguientePeriodo;
    }

    public void setFechaEstimadaSiguientePeriodo(LocalDate fechaEstimadaSiguientePeriodo) {
        this.fechaEstimadaSiguientePeriodo = fechaEstimadaSiguientePeriodo;
    }

    public Integer getDiasParaSiguientePeriodo() {
        return diasParaSiguientePeriodo;
    }

    public void setDiasParaSiguientePeriodo(Integer diasParaSiguientePeriodo) {
        this.diasParaSiguientePeriodo = diasParaSiguientePeriodo;
    }

    public Boolean getPeriodoRetrasado() {
        return periodoRetrasado;
    }

    public void setPeriodoRetrasado(Boolean periodoRetrasado) {
        this.periodoRetrasado = periodoRetrasado;
    }

    public Integer getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(Integer diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public LocalDate getFechaEstimadaOvulacion() {
        return fechaEstimadaOvulacion;
    }

    public void setFechaEstimadaOvulacion(LocalDate fechaEstimadaOvulacion) {
        this.fechaEstimadaOvulacion = fechaEstimadaOvulacion;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getMetodoCalculo() {
        return metodoCalculo;
    }

    public void setMetodoCalculo(String metodoCalculo) {
        this.metodoCalculo = metodoCalculo;
    }
}
