package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "menstruacion")
public class Menstruacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmenstruacion")
    private Long idMenstruacion;

    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechafin")
    private LocalDate fechaFin;

    @Column(name = "fechaactualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToOne
    @JoinColumn(name = "ciclo_menstrual_idciclomenstrual", nullable = false, unique = true)
    private CicloMenstrual cicloMenstrual;

    public Menstruacion() {
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public CicloMenstrual getCicloMenstrual() {
        return cicloMenstrual;
    }

    public void setCicloMenstrual(CicloMenstrual cicloMenstrual) {
        this.cicloMenstrual = cicloMenstrual;
    }

    public Integer getDuracion() {
        if (fechaInicio == null || fechaFin == null) {
            return null;
        }
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return Integer.valueOf((int) dias);
    }

    public boolean contieneFecha(LocalDate fecha) {
        if (fecha.isBefore(fechaInicio)) {
            return false;
        }
        if (fechaFin == null) {
            return true;
        }
        return !fecha.isAfter(fechaFin);
    }

    public String getEstado() {
        if (cicloMenstrual != null && "ESTIMADO".equals(cicloMenstrual.getEstado())) {
            return "ESTIMADA";
        }
        return "CONFIRMADA";
    }
}
