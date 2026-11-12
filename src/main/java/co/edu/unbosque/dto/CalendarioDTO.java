package co.edu.unbosque.dto;

import java.util.ArrayList;
import java.util.List;

public class CalendarioDTO {
    private Integer anio;

    private Integer mes;

    private List<DiaCalendarioDTO> dias = new ArrayList<DiaCalendarioDTO>();

    private CicloActualDTO cicloActual;

    private List<MenstruacionDTO> proximasMenstruaciones = new ArrayList<MenstruacionDTO>();

    public CalendarioDTO() {
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

    public List<DiaCalendarioDTO> getDias() {
        return dias;
    }

    public void setDias(List<DiaCalendarioDTO> dias) {
        this.dias = dias;
    }

    public CicloActualDTO getCicloActual() {
        return cicloActual;
    }

    public void setCicloActual(CicloActualDTO cicloActual) {
        this.cicloActual = cicloActual;
    }

    public List<MenstruacionDTO> getProximasMenstruaciones() {
        return proximasMenstruaciones;
    }

    public void setProximasMenstruaciones(List<MenstruacionDTO> proximasMenstruaciones) {
        this.proximasMenstruaciones = proximasMenstruaciones;
    }
}
