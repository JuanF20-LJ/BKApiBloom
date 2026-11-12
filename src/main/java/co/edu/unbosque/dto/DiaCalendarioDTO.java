package co.edu.unbosque.dto;

import java.time.LocalDate;

public class DiaCalendarioDTO {
    private LocalDate fecha;

    private String indicador;

    private Boolean tieneRegistro;

    private Long idRegistroDiario;

    private Boolean esEditable;

    private Boolean esHoy;

    private Integer diaDelCiclo;

    public DiaCalendarioDTO() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Boolean getTieneRegistro() {
        return tieneRegistro;
    }

    public void setTieneRegistro(Boolean tieneRegistro) {
        this.tieneRegistro = tieneRegistro;
    }

    public Long getIdRegistroDiario() {
        return idRegistroDiario;
    }

    public void setIdRegistroDiario(Long idRegistroDiario) {
        this.idRegistroDiario = idRegistroDiario;
    }

    public Boolean getEsEditable() {
        return esEditable;
    }

    public void setEsEditable(Boolean esEditable) {
        this.esEditable = esEditable;
    }

    public Boolean getEsHoy() {
        return esHoy;
    }

    public void setEsHoy(Boolean esHoy) {
        this.esHoy = esHoy;
    }

    public Integer getDiaDelCiclo() {
        return diaDelCiclo;
    }

    public void setDiaDelCiclo(Integer diaDelCiclo) {
        this.diaDelCiclo = diaDelCiclo;
    }
}
