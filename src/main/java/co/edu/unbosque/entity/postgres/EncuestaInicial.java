package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "encuesta_inicial")
public class EncuestaInicial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idencuestainicial")
    private Long idEncuestaInicial;

    @Column(name = "fecharealizacion", nullable = false)
    private LocalDateTime fechaRealizacion;

    @Column(name = "ingresorango", nullable = false)
    private Boolean ingresoRango;

    @Column(name = "duracionciclo")
    private Integer duracionCiclo;

    @Column(name = "duracionmenstruacion")
    private Integer duracionMenstruacion;

    @Column(name = "cantidadciclo")
    private Integer cantidadCiclosIngresados;

    @OneToOne
    @JoinColumn(name = "usuaria_idusuario", nullable = false, unique = true)
    private Usuaria usuaria;

    public EncuestaInicial() {
    }

    public Long getIdEncuestaInicial() {
        return idEncuestaInicial;
    }

    public void setIdEncuestaInicial(Long idEncuestaInicial) {
        this.idEncuestaInicial = idEncuestaInicial;
    }

    public LocalDateTime getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDateTime fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Boolean getIngresoRango() {
        return ingresoRango;
    }

    public void setIngresoRango(Boolean ingresoRango) {
        this.ingresoRango = ingresoRango;
    }

    public Integer getDuracionCiclo() {
        return duracionCiclo;
    }

    public void setDuracionCiclo(Integer duracionCiclo) {
        this.duracionCiclo = duracionCiclo;
    }

    public Integer getDuracionMenstruacion() {
        return duracionMenstruacion;
    }

    public void setDuracionMenstruacion(Integer duracionMenstruacion) {
        this.duracionMenstruacion = duracionMenstruacion;
    }

    public Integer getCantidadCiclosIngresados() {
        return cantidadCiclosIngresados;
    }

    public void setCantidadCiclosIngresados(Integer cantidadCiclosIngresados) {
        this.cantidadCiclosIngresados = cantidadCiclosIngresados;
    }

    public Usuaria getUsuaria() {
        return usuaria;
    }

    public void setUsuaria(Usuaria usuaria) {
        this.usuaria = usuaria;
    }
}
