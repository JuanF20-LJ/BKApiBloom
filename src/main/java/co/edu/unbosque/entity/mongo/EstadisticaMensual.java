package co.edu.unbosque.entity.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "estadisticas_mensuales")
@CompoundIndex(name = "usuaria_anio_mes", def = "{'idUsuario': 1, 'anio': 1, 'mes': 1}", unique = true)
public class EstadisticaMensual {
    @Id
    private String idEstadisticaMensual;

    @Field("schemaVersion")
    private Integer schemaVersion;

    @Field("idUsuario")
    @Indexed
    private Long idUsuario;

    @Field("anio")
    private Integer anio;

    @Field("mes")
    private Integer mes;

    @Field("diasRegistrados")
    private Integer diasRegistrados;

    @Field("diasMenstruacion")
    private Integer diasMenstruacion;

    @Field("ciclosFinalizados")
    private List<CicloResumen> ciclosFinalizados = new ArrayList<CicloResumen>();

    @Field("duracionesPeriodo")
    private List<Integer> duracionesPeriodo = new ArrayList<Integer>();

    @Field("sintomas")
    private List<SintomaConteo> sintomas = new ArrayList<SintomaConteo>();

    @Field("fechaCalculo")
    private LocalDateTime fechaCalculo;

    public EstadisticaMensual() {
    }

    public String getIdEstadisticaMensual() {
        return idEstadisticaMensual;
    }

    public void setIdEstadisticaMensual(String idEstadisticaMensual) {
        this.idEstadisticaMensual = idEstadisticaMensual;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDiasRegistrados() {
        return diasRegistrados;
    }

    public void setDiasRegistrados(Integer diasRegistrados) {
        this.diasRegistrados = diasRegistrados;
    }

    public Integer getDiasMenstruacion() {
        return diasMenstruacion;
    }

    public void setDiasMenstruacion(Integer diasMenstruacion) {
        this.diasMenstruacion = diasMenstruacion;
    }

    public List<CicloResumen> getCiclosFinalizados() {
        return ciclosFinalizados;
    }

    public void setCiclosFinalizados(List<CicloResumen> ciclosFinalizados) {
        this.ciclosFinalizados = ciclosFinalizados;
    }

    public List<Integer> getDuracionesPeriodo() {
        return duracionesPeriodo;
    }

    public void setDuracionesPeriodo(List<Integer> duracionesPeriodo) {
        this.duracionesPeriodo = duracionesPeriodo;
    }

    public List<SintomaConteo> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<SintomaConteo> sintomas) {
        this.sintomas = sintomas;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }
}
