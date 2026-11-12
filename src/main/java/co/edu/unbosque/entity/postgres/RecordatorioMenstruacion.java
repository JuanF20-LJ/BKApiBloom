package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("MENSTRUACION")
public class RecordatorioMenstruacion extends Recordatorio {
    @Column(name = "diasanticipacion")
    private Integer diasAnticipacion;

    @ManyToOne
    @JoinColumn(name = "ciclo_menstrual_idciclomenstrual")
    private CicloMenstrual cicloMenstrual;

    public RecordatorioMenstruacion() {
        setTipoRecordatorio("MENSTRUACION");
    }

    public Integer getDiasAnticipacion() {
        return diasAnticipacion;
    }

    public void setDiasAnticipacion(Integer diasAnticipacion) {
        this.diasAnticipacion = diasAnticipacion;
    }

    public CicloMenstrual getCicloMenstrual() {
        return cicloMenstrual;
    }

    public void setCicloMenstrual(CicloMenstrual cicloMenstrual) {
        this.cicloMenstrual = cicloMenstrual;
    }

    @Override
    public Long obtenerIdUsuario() {
        return cicloMenstrual.getUsuaria().getIdUsuario();
    }
}
