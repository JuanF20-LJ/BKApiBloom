package co.edu.unbosque.entity.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "estadisticas_resumen")
public class EstadisticaResumen {
    @Id
    private Long idUsuario;

    @Field("schemaVersion")
    private Integer schemaVersion;

    @Field("fechaCalculo")
    private LocalDateTime fechaCalculo;

    @Field("totalCiclos")
    private Integer totalCiclos;

    @Field("promedioDuracionCiclo")
    private Double promedioDuracionCiclo;

    @Field("promedioDuracionPeriodo")
    private Double promedioDuracionPeriodo;

    @Field("cicloMasCorto")
    private CicloResumen cicloMasCorto;

    @Field("cicloMasLargo")
    private CicloResumen cicloMasLargo;

    @Field("sintomasFrecuentes")
    private List<SintomaConteo> sintomasFrecuentes = new ArrayList<SintomaConteo>();

    @Field("totalDiasRegistrados")
    private Integer totalDiasRegistrados;

    public EstadisticaResumen() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }

    public Integer getTotalCiclos() {
        return totalCiclos;
    }

    public void setTotalCiclos(Integer totalCiclos) {
        this.totalCiclos = totalCiclos;
    }

    public Double getPromedioDuracionCiclo() {
        return promedioDuracionCiclo;
    }

    public void setPromedioDuracionCiclo(Double promedioDuracionCiclo) {
        this.promedioDuracionCiclo = promedioDuracionCiclo;
    }

    public Double getPromedioDuracionPeriodo() {
        return promedioDuracionPeriodo;
    }

    public void setPromedioDuracionPeriodo(Double promedioDuracionPeriodo) {
        this.promedioDuracionPeriodo = promedioDuracionPeriodo;
    }

    public CicloResumen getCicloMasCorto() {
        return cicloMasCorto;
    }

    public void setCicloMasCorto(CicloResumen cicloMasCorto) {
        this.cicloMasCorto = cicloMasCorto;
    }

    public CicloResumen getCicloMasLargo() {
        return cicloMasLargo;
    }

    public void setCicloMasLargo(CicloResumen cicloMasLargo) {
        this.cicloMasLargo = cicloMasLargo;
    }

    public List<SintomaConteo> getSintomasFrecuentes() {
        return sintomasFrecuentes;
    }

    public void setSintomasFrecuentes(List<SintomaConteo> sintomasFrecuentes) {
        this.sintomasFrecuentes = sintomasFrecuentes;
    }

    public Integer getTotalDiasRegistrados() {
        return totalDiasRegistrados;
    }

    public void setTotalDiasRegistrados(Integer totalDiasRegistrados) {
        this.totalDiasRegistrados = totalDiasRegistrados;
    }
}
