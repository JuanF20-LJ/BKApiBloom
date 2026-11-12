package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroDiarioDTO {
    private Long idRegistroDiario;

    private LocalDate fecha;

    private Boolean hayMenstruacion;

    private String tipoFlujo;

    private String nota;

    private List<SintomaDTO> sintomas = new ArrayList<SintomaDTO>();

    private List<EmocionDTO> emociones = new ArrayList<EmocionDTO>();

    private List<ActividadSexualDTO> actividadesSexuales = new ArrayList<ActividadSexualDTO>();

    private Long idCicloMenstrual;

    private Long idMenstruacion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    public RegistroDiarioDTO() {
    }

    public Long getIdRegistroDiario() {
        return idRegistroDiario;
    }

    public void setIdRegistroDiario(Long idRegistroDiario) {
        this.idRegistroDiario = idRegistroDiario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getHayMenstruacion() {
        return hayMenstruacion;
    }

    public void setHayMenstruacion(Boolean hayMenstruacion) {
        this.hayMenstruacion = hayMenstruacion;
    }

    public String getTipoFlujo() {
        return tipoFlujo;
    }

    public void setTipoFlujo(String tipoFlujo) {
        this.tipoFlujo = tipoFlujo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public List<SintomaDTO> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<SintomaDTO> sintomas) {
        this.sintomas = sintomas;
    }

    public List<EmocionDTO> getEmociones() {
        return emociones;
    }

    public void setEmociones(List<EmocionDTO> emociones) {
        this.emociones = emociones;
    }

    public List<ActividadSexualDTO> getActividadesSexuales() {
        return actividadesSexuales;
    }

    public void setActividadesSexuales(List<ActividadSexualDTO> actividadesSexuales) {
        this.actividadesSexuales = actividadesSexuales;
    }

    public Long getIdCicloMenstrual() {
        return idCicloMenstrual;
    }

    public void setIdCicloMenstrual(Long idCicloMenstrual) {
        this.idCicloMenstrual = idCicloMenstrual;
    }

    public Long getIdMenstruacion() {
        return idMenstruacion;
    }

    public void setIdMenstruacion(Long idMenstruacion) {
        this.idMenstruacion = idMenstruacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
