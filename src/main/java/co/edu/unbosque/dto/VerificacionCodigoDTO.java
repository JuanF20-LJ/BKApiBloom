package co.edu.unbosque.dto;

public class VerificacionCodigoDTO {
    private String correoElectronico;

    private String codigo;

    public VerificacionCodigoDTO() {
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
