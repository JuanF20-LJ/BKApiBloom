package co.edu.unbosque.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuariaDTO {
    private Long idUsuario;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private String apodo;

    private LocalDate fechaNacimiento;

    private String correoElectronico;

    private LocalDateTime fechaRegistro;

    private Double altura;

    private Double peso;

    private Double horasSueno;

    private String metodoCalculo;

    private Boolean cuentaVerificada;

    private Boolean encuestaCompletada;

    private LocalDateTime fechaUltimoAcceso;

    public UsuariaDTO() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public Boolean getCuentaVerificada() {
        return cuentaVerificada;
    }

    public void setCuentaVerificada(Boolean cuentaVerificada) {
        this.cuentaVerificada = cuentaVerificada;
    }

    public Boolean getEncuestaCompletada() {
        return encuestaCompletada;
    }

    public void setEncuestaCompletada(Boolean encuestaCompletada) {
        this.encuestaCompletada = encuestaCompletada;
    }

    public LocalDateTime getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
        this.fechaUltimoAcceso = fechaUltimoAcceso;
    }
}
