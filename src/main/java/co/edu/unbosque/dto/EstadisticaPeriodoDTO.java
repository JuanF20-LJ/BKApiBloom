package co.edu.unbosque.dto;

import java.util.ArrayList;
import java.util.List;

public class EstadisticaPeriodoDTO {
    private String desde;

    private String hasta;

    private Integer diasRegistrados;

    private Integer diasMenstruacion;

    private Integer totalCiclos;

    private Double promedioDuracionCiclo;

    private Double promedioDuracionPeriodo;

    private CicloResumenDTO cicloMasCorto;

    private CicloResumenDTO cicloMasLargo;

    private List<SintomaConteoDTO> sintomasFrecuentes = new ArrayList<SintomaConteoDTO>();

    public EstadisticaPeriodoDTO() {
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
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
}
