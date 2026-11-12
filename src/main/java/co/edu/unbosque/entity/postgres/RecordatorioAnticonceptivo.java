package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ANTICONCEPTIVO")
public class RecordatorioAnticonceptivo extends Recordatorio {
    @Column(name = "fechaaplicacion")
    private java.time.LocalDate fechaAplicacion;

    @ManyToOne
    @JoinColumn(name = "anticonceptivo_idanticonceptivo")
    private Anticonceptivo anticonceptivo;

    public RecordatorioAnticonceptivo() {
        setTipoRecordatorio("ANTICONCEPTIVO");
    }

    public java.time.LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(java.time.LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Anticonceptivo getAnticonceptivo() {
        return anticonceptivo;
    }

    public void setAnticonceptivo(Anticonceptivo anticonceptivo) {
        this.anticonceptivo = anticonceptivo;
    }

    @Override
    public Long obtenerIdUsuario() {
        return anticonceptivo.getUsuaria().getIdUsuario();
    }
}
