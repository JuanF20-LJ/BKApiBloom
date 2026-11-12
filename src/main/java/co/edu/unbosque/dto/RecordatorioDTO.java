package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecordatorioDTO {
    private Long idRecordatorio;

    private String tipoRecordatorio;

    private String mensaje;

    private LocalDateTime fechaProgramado;

    private LocalDateTime fechaCreacion;

    private Boolean leido;

    private Integer diasAnticipacion;

    private Long idCicloMenstrual;

    private LocalDate fechaAplicacion;

    private Long idAnticonceptivo;

    public RecordatorioDTO() {
    }

    public Long getIdRecordatorio() {
        return idRecordatorio;
    }

    public void setIdRecordatorio(Long idRecordatorio) {
        this.idRecordatorio = idRecordatorio;
    }

    public String getTipoRecordatorio() {
        return tipoRecordatorio;
    }

    public void setTipoRecordatorio(String tipoRecordatorio) {
        this.tipoRecordatorio = tipoRecordatorio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaProgramado() {
        return fechaProgramado;
    }

    public void setFechaProgramado(LocalDateTime fechaProgramado) {
        this.fechaProgramado = fechaProgramado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Integer getDiasAnticipacion() {
        return diasAnticipacion;
    }

    public void setDiasAnticipacion(Integer diasAnticipacion) {
        this.diasAnticipacion = diasAnticipacion;
    }

    public Long getIdCicloMenstrual() {
        return idCicloMenstrual;
    }

    public void setIdCicloMenstrual(Long idCicloMenstrual) {
        this.idCicloMenstrual = idCicloMenstrual;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Long getIdAnticonceptivo() {
        return idAnticonceptivo;
    }

    public void setIdAnticonceptivo(Long idAnticonceptivo) {
        this.idAnticonceptivo = idAnticonceptivo;
    }
}
