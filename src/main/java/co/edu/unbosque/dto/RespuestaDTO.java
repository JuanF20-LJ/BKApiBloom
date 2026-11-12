package co.edu.unbosque.dto;

public class RespuestaDTO {
    private Integer codigo;

    private String mensaje;

    private Object datos;

    public RespuestaDTO() {
    }

    public RespuestaDTO(Integer codigo, String mensaje, Object datos) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }
}
