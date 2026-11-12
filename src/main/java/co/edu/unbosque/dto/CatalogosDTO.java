package co.edu.unbosque.dto;

import java.util.ArrayList;
import java.util.List;

public class CatalogosDTO {
    private List<SintomaDTO> sintomas = new ArrayList<SintomaDTO>();

    private List<EmocionDTO> emociones = new ArrayList<EmocionDTO>();

    private List<ActividadSexualDTO> actividadesSexuales = new ArrayList<ActividadSexualDTO>();

    public CatalogosDTO() {
    }

    public List<SintomaDTO> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<SintomaDTO> sintomas) {
        this.sintomas = sintomas;
    }

    public List<EmocionDTO> getEmociones() {
        return emociones;
    }

    public void setEmociones(List<EmocionDTO> emociones) {
        this.emociones = emociones;
    }

    public List<ActividadSexualDTO> getActividadesSexuales() {
        return actividadesSexuales;
    }

    public void setActividadesSexuales(List<ActividadSexualDTO> actividadesSexuales) {
        this.actividadesSexuales = actividadesSexuales;
    }
}
