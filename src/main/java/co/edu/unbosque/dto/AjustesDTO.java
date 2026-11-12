package co.edu.unbosque.dto;

public class AjustesDTO {
    private String correoElectronico;

    private String primerNombre;

    private String primerApellido;

    private String apodo;

    private Double altura;

    private Double peso;

    private Double horasSueno;

    private String metodoCalculo;

    private Integer duracionCiclo;

    private Integer duracionMenstruacion;

    public AjustesDTO() {
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
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
