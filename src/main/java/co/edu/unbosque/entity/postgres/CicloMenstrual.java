package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ciclo_menstrual")
public class CicloMenstrual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idciclomenstrual")
    private Long idCicloMenstrual;

    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechafin")
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "origen", nullable = false, length = 15)
    private String origen;

    @Column(name = "fechaactualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "usuaria_idusuario", nullable = false)
    private Usuaria usuaria;

    @OneToOne(mappedBy = "cicloMenstrual")
    private Menstruacion menstruacion;

    public CicloMenstrual() {
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Usuaria getUsuaria() {
        return usuaria;
    }

    public void setUsuaria(Usuaria usuaria) {
        this.usuaria = usuaria;
    }

    public Menstruacion getMenstruacion() {
        return menstruacion;
    }

    public void setMenstruacion(Menstruacion menstruacion) {
        this.menstruacion = menstruacion;
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
}
