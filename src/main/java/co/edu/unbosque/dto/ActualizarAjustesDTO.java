package co.edu.unbosque.dto;

public class ActualizarAjustesDTO {
    private String apodo;

    private Double altura;

    private Double peso;

    private Double horasSueno;

    private String metodoCalculo;

    private Integer duracionCiclo;

    private Integer duracionMenstruacion;

    public ActualizarAjustesDTO() {
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
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

    public String getMetodoCalculo() {
        return metodoCalculo;
    }

    public void setMetodoCalculo(String metodoCalculo) {
        this.metodoCalculo = metodoCalculo;
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
}
