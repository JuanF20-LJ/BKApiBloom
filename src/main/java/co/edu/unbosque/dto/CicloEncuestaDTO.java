package co.edu.unbosque.dto;

import java.time.LocalDate;

public class CicloEncuestaDTO {
    private LocalDate fechaInicioMenstruacion;

    private LocalDate fechaFinMenstruacion;

    public CicloEncuestaDTO() {
    }

    public LocalDate getFechaInicioMenstruacion() {
        return fechaInicioMenstruacion;
    }

    public void setFechaInicioMenstruacion(LocalDate fechaInicioMenstruacion) {
        this.fechaInicioMenstruacion = fechaInicioMenstruacion;
    }

    public LocalDate getFechaFinMenstruacion() {
        return fechaFinMenstruacion;
    }

    public void setFechaFinMenstruacion(LocalDate fechaFinMenstruacion) {
        this.fechaFinMenstruacion = fechaFinMenstruacion;
    }
}
