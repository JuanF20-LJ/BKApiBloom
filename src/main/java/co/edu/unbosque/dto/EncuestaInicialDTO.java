package co.edu.unbosque.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EncuestaInicialDTO {
    private Long idEncuestaInicial;

    private LocalDateTime fechaRealizacion;

    private Double altura;

    private Double peso;

    private Double horasSueno;

    private Boolean ingresoRango;

    private Integer duracionCiclo;

    private Integer duracionMenstruacion;

    private Integer cantidadCiclosIngresados;

    private List<CicloEncuestaDTO> ciclosIniciales = new ArrayList<CicloEncuestaDTO>();

    public EncuestaInicialDTO() {
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

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getHorasSueno() {
        return horasSueno;
    }

    public void setHorasSueno(Double horasSueno) {
        this.horasSueno = horasSueno;
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

    public List<CicloEncuestaDTO> getCiclosIniciales() {
        return ciclosIniciales;
    }

    public void setCiclosIniciales(List<CicloEncuestaDTO> ciclosIniciales) {
        this.ciclosIniciales = ciclosIniciales;
    }
}
