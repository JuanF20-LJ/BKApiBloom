package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registro_diario")
public class RegistroDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idregistrodiario")
    private Long idRegistroDiario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "haymenstruacion", nullable = false)
    private Boolean hayMenstruacion;

    @Column(name = "tipoflujo", length = 10)
    private String tipoFlujo;

    @Column(name = "nota", length = 4000)
    private String nota;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechamodificacion")
    private LocalDateTime fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "usuaria_idusuario", nullable = false)
    private Usuaria usuaria;

    @ManyToOne
    @JoinColumn(name = "ciclo_menstrual_idciclomenstrual")
    private CicloMenstrual cicloMenstrual;

    @ManyToOne
    @JoinColumn(name = "menstruacion_idmenstruacion")
    private Menstruacion menstruacion;

    @ManyToMany
    @JoinTable(name = "registro_sintoma", joinColumns = @JoinColumn(name = "registro_diario_idregistrodiario"), inverseJoinColumns = @JoinColumn(name = "sintoma_idsintoma"))
    private List<Sintoma> sintomas = new ArrayList<Sintoma>();

    @ManyToMany
    @JoinTable(name = "registro_emocion", joinColumns = @JoinColumn(name = "registro_diario_idregistrodiario"), inverseJoinColumns = @JoinColumn(name = "emocion_idemocion"))
    private List<Emocion> emociones = new ArrayList<Emocion>();

    @ManyToMany
    @JoinTable(name = "registro_actividad_sexual", joinColumns = @JoinColumn(name = "registro_diario_idregistrodiario"), inverseJoinColumns = @JoinColumn(name = "actividad_sexual_idactividadsexual"))
    private List<ActividadSexual> actividadesSexuales = new ArrayList<ActividadSexual>();

    public RegistroDiario() {
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

    public Usuaria getUsuaria() {
        return usuaria;
    }

    public void setUsuaria(Usuaria usuaria) {
        this.usuaria = usuaria;
    }

    public CicloMenstrual getCicloMenstrual() {
        return cicloMenstrual;
    }

    public void setCicloMenstrual(CicloMenstrual cicloMenstrual) {
        this.cicloMenstrual = cicloMenstrual;
    }

    public Menstruacion getMenstruacion() {
        return menstruacion;
    }

    public void setMenstruacion(Menstruacion menstruacion) {
        this.menstruacion = menstruacion;
    }

    public List<Sintoma> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<Sintoma> sintomas) {
        this.sintomas = sintomas;
    }

    public List<Emocion> getEmociones() {
        return emociones;
    }

    public void setEmociones(List<Emocion> emociones) {
        this.emociones = emociones;
    }

    public List<ActividadSexual> getActividadesSexuales() {
        return actividadesSexuales;
    }

    public void setActividadesSexuales(List<ActividadSexual> actividadesSexuales) {
        this.actividadesSexuales = actividadesSexuales;
    }
}
