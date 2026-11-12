package co.edu.unbosque.service;

import co.edu.unbosque.dto.CalendarioDTO;
import co.edu.unbosque.dto.CicloActualDTO;
import co.edu.unbosque.dto.DiaCalendarioDTO;
import co.edu.unbosque.entity.postgres.CicloMenstrual;
import co.edu.unbosque.entity.postgres.RegistroDiario;
import co.edu.unbosque.repository.postgres.CicloMenstrualRepository;
import co.edu.unbosque.repository.postgres.RegistroDiarioRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalendarioService {
    public static final String PERIODO_CONFIRMADO = "PERIODO_CONFIRMADO";

    public static final String PERIODO_PRONOSTICADO = "PERIODO_PRONOSTICADO";

    public static final String PERIODO_RETRASADO = "PERIODO_RETRASADO";

    public static final String DIA_OVULACION = "DIA_OVULACION";

    public static final String FASE_OVULACION = "FASE_OVULACION";

    public static final String NINGUNO = "NINGUNO";

    @Autowired
    private CicloMenstrualRepository cicloMenstrualRepository;

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private CicloMenstrualService cicloMenstrualService;

    public CalendarioDTO llenarCalendario(Long idUsuario, int anio, int mes) {
        if (mes < 1 || mes > 12 || anio < 1900 || anio > 2200) {
            return null;
        }
        YearMonth anioMes = YearMonth.of(anio, mes);
        LocalDate primerDia = anioMes.atDay(1);
        LocalDate ultimoDia = anioMes.atEndOfMonth();
        LocalDate hoy = LocalDate.now();

        List<CicloMenstrual> ciclos = cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario);
        List<RegistroDiario> registros = registroDiarioRepository.findByUsuariaIdUsuarioAndFechaBetweenOrderByFechaAsc(idUsuario, primerDia, ultimoDia);
        CicloActualDTO cicloActual = cicloMenstrualService.obtenerCicloActual(idUsuario);

        LocalDate inicioPrimerEstimado = null;
        for (CicloMenstrual ciclo : ciclos) {
            if (CicloMenstrualService.CICLO_ESTIMADO.equals(ciclo.getEstado()) && inicioPrimerEstimado == null) {
                inicioPrimerEstimado = ciclo.getFechaInicio();
            }
        }

        LocalDate retrasoDesde = null;
        if (cicloActual != null && Boolean.TRUE.equals(cicloActual.getPeriodoRetrasado())) {
            retrasoDesde = hoy.minusDays(cicloActual.getDiasRetraso());
        }

        CalendarioDTO calendario = new CalendarioDTO();
        calendario.setAnio(anio);
        calendario.setMes(mes);
        calendario.setCicloActual(cicloActual);
        calendario.setProximasMenstruaciones(cicloMenstrualService.listarProximasMenstruaciones(idUsuario));

        LocalDate dia = primerDia;
        while (!dia.isAfter(ultimoDia)) {
            DiaCalendarioDTO diaDTO = new DiaCalendarioDTO();
            diaDTO.setFecha(dia);
            diaDTO.setEsHoy(dia.equals(hoy));
            diaDTO.setEsEditable(!dia.isAfter(hoy));
            diaDTO.setTieneRegistro(Boolean.FALSE);
            for (RegistroDiario registro : registros) {
                if (registro.getFecha().equals(dia)) {
                    diaDTO.setTieneRegistro(Boolean.TRUE);
                    diaDTO.setIdRegistroDiario(registro.getIdRegistroDiario());
                }
            }

            CicloMenstrual ciclo = buscarCicloDeFecha(ciclos, dia, inicioPrimerEstimado);
            String indicador = NINGUNO;
            if (ciclo != null) {
                diaDTO.setDiaDelCiclo(CicloMenstrualService.diasEntre(ciclo.getFechaInicio(), dia) + 1);
                boolean esMenstruacion = ciclo.getMenstruacion() != null && ciclo.getMenstruacion().contieneFecha(dia);
                LocalDate ovulacion = calcularOvulacion(ciclo, inicioPrimerEstimado, cicloActual);

                if (esMenstruacion && CicloMenstrualService.MENSTRUACION_CONFIRMADA.equals(ciclo.getMenstruacion().getEstado())) {
                    indicador = PERIODO_CONFIRMADO;
                } else if (retrasoDesde != null && !dia.isBefore(retrasoDesde) && !dia.isAfter(hoy)) {
                    indicador = PERIODO_RETRASADO;
                } else if (esMenstruacion) {
                    indicador = PERIODO_PRONOSTICADO;
                } else if (ovulacion != null && dia.equals(ovulacion)) {
                    indicador = DIA_OVULACION;
                } else if (ovulacion != null && cicloMenstrualService.estaEnVentanaOvulacion(dia, ovulacion)) {
                    indicador = FASE_OVULACION;
                }
            }
            diaDTO.setIndicador(indicador);
            calendario.getDias().add(diaDTO);
            dia = dia.plusDays(1);
        }
        return calendario;
    }

    private CicloMenstrual buscarCicloDeFecha(List<CicloMenstrual> ciclos, LocalDate fecha, LocalDate inicioPrimerEstimado) {
        for (CicloMenstrual ciclo : ciclos) {
            if (fecha.isBefore(ciclo.getFechaInicio())) {
                continue;
            }
            LocalDate fin = ciclo.getFechaFin();
            if (fin == null && inicioPrimerEstimado != null) {
                fin = inicioPrimerEstimado.minusDays(1);
            }
            if (fin == null || !fecha.isAfter(fin)) {
                return ciclo;
            }
        }
        return null;
    }

    private LocalDate calcularOvulacion(CicloMenstrual ciclo, LocalDate inicioPrimerEstimado, CicloActualDTO cicloActual) {
        LocalDate inicioSiguiente = null;
        if (ciclo.getFechaFin() != null) {
            inicioSiguiente = ciclo.getFechaFin().plusDays(1);
        } else if (inicioPrimerEstimado != null) {
            inicioSiguiente = inicioPrimerEstimado;
        } else if (cicloActual != null) {
            inicioSiguiente = cicloActual.getFechaEstimadaSiguientePeriodo();
        }
        if (inicioSiguiente == null) {
            return null;
        }
        return inicioSiguiente.minusDays(CicloMenstrualService.DIAS_OVULACION_ANTES_DEL_PERIODO);
    }
}
