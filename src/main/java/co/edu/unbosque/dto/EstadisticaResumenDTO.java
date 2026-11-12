package co.edu.unbosque.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaResumenDTO {
    private Long idUsuario;

    private LocalDateTime fechaCalculo;

    private Integer totalCiclos;

    private Double promedioDuracionCiclo;

    private Double promedioDuracionPeriodo;

    private CicloResumenDTO cicloMasCorto;

    private CicloResumenDTO cicloMasLargo;

    private List<SintomaConteoDTO> sintomasFrecuentes = new ArrayList<SintomaConteoDTO>();

    private Integer totalDiasRegistrados;

    public EstadisticaResumenDTO() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public CicloResumenDTO getCicloMasCorto() {
        return cicloMasCorto;
    }

    public void setCicloMasCorto(CicloResumenDTO cicloMasCorto) {
        this.cicloMasCorto = cicloMasCorto;
    }

    public CicloResumenDTO getCicloMasLargo() {
        return cicloMasLargo;
    }

    public void setCicloMasLargo(CicloResumenDTO cicloMasLargo) {
        this.cicloMasLargo = cicloMasLargo;
    }

    public List<SintomaConteoDTO> getSintomasFrecuentes() {
        return sintomasFrecuentes;
    }

    public void setSintomasFrecuentes(List<SintomaConteoDTO> sintomasFrecuentes) {
        this.sintomasFrecuentes = sintomasFrecuentes;
    }

    public Integer getTotalDiasRegistrados() {
        return totalDiasRegistrados;
    }

    public void setTotalDiasRegistrados(Integer totalDiasRegistrados) {
        this.totalDiasRegistrados = totalDiasRegistrados;
    }
}
