package co.edu.unbosque.service;

import co.edu.unbosque.dto.CicloActualDTO;
import co.edu.unbosque.dto.CicloMenstrualDTO;
import co.edu.unbosque.dto.MenstruacionDTO;
import co.edu.unbosque.entity.postgres.CicloMenstrual;
import co.edu.unbosque.entity.postgres.EncuestaInicial;
import co.edu.unbosque.entity.postgres.Menstruacion;
import co.edu.unbosque.entity.postgres.RegistroDiario;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.postgres.CicloMenstrualRepository;
import co.edu.unbosque.repository.postgres.EncuestaInicialRepository;
import co.edu.unbosque.repository.postgres.MenstruacionRepository;
import co.edu.unbosque.repository.postgres.RegistroDiarioRepository;
import co.edu.unbosque.repository.postgres.UsuariaRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CicloMenstrualService {
    public static final String CICLO_EN_CURSO = "EN_CURSO";

    public static final String CICLO_FINALIZADO = "FINALIZADO";

    public static final String CICLO_ESTIMADO = "ESTIMADO";

    public static final String MENSTRUACION_CONFIRMADA = "CONFIRMADA";

    public static final String MENSTRUACION_ESTIMADA = "ESTIMADA";

    public static final String ORIGEN_ENCUESTA = "ENCUESTA";

    public static final String ORIGEN_CALENDARIO = "CALENDARIO";

    public static final String ORIGEN_PREDICCION = "PREDICCION";

    public static final int CICLOS_A_PREDECIR = 3;

    public static final int CICLOS_PARA_PROMEDIO = 6;

    public static final int DURACION_CICLO_POR_DEFECTO = 28;

    public static final int DURACION_MENSTRUACION_POR_DEFECTO = 5;

    public static final int TOLERANCIA_DIAS_SIN_REGISTRO = 1;

    public static final int DIAS_ANTICIPACION_RECORDATORIO = 2;

    public static final int DIAS_OVULACION_ANTES_DEL_PERIODO = 14;

    @Autowired
    private CicloMenstrualRepository cicloMenstrualRepository;

    @Autowired
    private MenstruacionRepository menstruacionRepository;

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private EncuestaInicialRepository encuestaInicialRepository;

    @Autowired
    private RecordatorioService recordatorioService;

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<Menstruacion, MenstruacionDTO>() {
            @Override
            protected void configure() {
                map().setIdCicloMenstrual(source.getCicloMenstrual().getIdCicloMenstrual());
            }
        });
    }

    private static class PeriodoMenstrual {
        private LocalDate inicio;

        private LocalDate fin;

        private String origen;

        PeriodoMenstrual(LocalDate inicio, LocalDate fin, String origen) {
            this.inicio = inicio;
            this.fin = fin;
            this.origen = origen;
        }
    }

    public void crearCicloDeEncuesta(Usuaria usuaria, LocalDate inicioMenstruacion, LocalDate finMenstruacion) {
        CicloMenstrual ciclo = new CicloMenstrual();
        ciclo.setFechaInicio(inicioMenstruacion);
        ciclo.setEstado(CICLO_EN_CURSO);
        ciclo.setOrigen(ORIGEN_ENCUESTA);
        ciclo.setUsuaria(usuaria);
        ciclo.setFechaActualizacion(LocalDateTime.now());
        cicloMenstrualRepository.save(ciclo);

        Menstruacion menstruacion = new Menstruacion();
        menstruacion.setFechaInicio(inicioMenstruacion);
        menstruacion.setFechaFin(finMenstruacion);
        menstruacion.setCicloMenstrual(ciclo);
        menstruacion.setFechaActualizacion(LocalDateTime.now());
        menstruacionRepository.save(menstruacion);
        ciclo.setMenstruacion(menstruacion);
        cicloMenstrualRepository.save(ciclo);
    }

    public void recalcularCiclos(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return;
        }
        List<CicloMenstrual> ciclosAnteriores = cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario);
        List<RegistroDiario> registros = registroDiarioRepository.findByUsuariaIdUsuarioOrderByFechaAsc(idUsuario);

        List<PeriodoMenstrual> periodos = new ArrayList<PeriodoMenstrual>();
        for (CicloMenstrual ciclo : ciclosAnteriores) {
            if (ORIGEN_ENCUESTA.equals(ciclo.getOrigen()) && ciclo.getMenstruacion() != null) {
                periodos.add(new PeriodoMenstrual(ciclo.getMenstruacion().getFechaInicio(), ciclo.getMenstruacion().getFechaFin(), ORIGEN_ENCUESTA));
            }
        }
        PeriodoMenstrual periodoActual = null;
        for (RegistroDiario registro : registros) {
            if (Boolean.TRUE.equals(registro.getHayMenstruacion())) {
                if (periodoActual != null && diasEntre(periodoActual.fin, registro.getFecha()) <= 1 + TOLERANCIA_DIAS_SIN_REGISTRO) {
                    periodoActual.fin = registro.getFecha();
                } else {
                    periodoActual = new PeriodoMenstrual(registro.getFecha(), registro.getFecha(), ORIGEN_CALENDARIO);
                    periodos.add(periodoActual);
                }
            } else {
                periodoActual = null;
            }
        }
        List<PeriodoMenstrual> periodosUnidos = unirPeriodos(periodos);

        recordatorioService.eliminarRecordatoriosMenstruacion(idUsuario);
        for (RegistroDiario registro : registros) {
            registro.setCicloMenstrual(null);
            registro.setMenstruacion(null);
        }
        menstruacionRepository.deleteAll(menstruacionRepository.findByCicloMenstrualUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario));
        cicloMenstrualRepository.deleteAll(ciclosAnteriores);

        List<CicloMenstrual> ciclosReales = new ArrayList<CicloMenstrual>();
        for (int i = 0; i < periodosUnidos.size(); i++) {
            PeriodoMenstrual periodo = periodosUnidos.get(i);
            CicloMenstrual ciclo = new CicloMenstrual();
            ciclo.setFechaInicio(periodo.inicio);
            if (i < periodosUnidos.size() - 1) {
                ciclo.setFechaFin(periodosUnidos.get(i + 1).inicio.minusDays(1));
                ciclo.setEstado(CICLO_FINALIZADO);
            } else {
                ciclo.setEstado(CICLO_EN_CURSO);
            }
            ciclo.setOrigen(periodo.origen);
            ciclo.setUsuaria(usuaria);
            ciclo.setFechaActualizacion(LocalDateTime.now());
            cicloMenstrualRepository.save(ciclo);
            crearMenstruacion(ciclo, periodo.inicio, periodo.fin);
            ciclosReales.add(ciclo);
        }

        for (RegistroDiario registro : registros) {
            for (CicloMenstrual ciclo : ciclosReales) {
                if (ciclo.contieneFecha(registro.getFecha())) {
                    registro.setCicloMenstrual(ciclo);
                    if (ciclo.getMenstruacion().contieneFecha(registro.getFecha())) {
                        registro.setMenstruacion(ciclo.getMenstruacion());
                    }
                }
            }
            registroDiarioRepository.save(registro);
        }

        generarPredicciones(usuaria, ciclosReales);
        estadisticaService.recalcularEstadisticas(idUsuario);
    }

    private List<PeriodoMenstrual> unirPeriodos(List<PeriodoMenstrual> periodos) {
        Collections.sort(periodos, new Comparator<PeriodoMenstrual>() {
            @Override
            public int compare(PeriodoMenstrual a, PeriodoMenstrual b) {
                return a.inicio.compareTo(b.inicio);
            }
        });
        List<PeriodoMenstrual> unidos = new ArrayList<PeriodoMenstrual>();
        PeriodoMenstrual ultimo = null;
        for (PeriodoMenstrual periodo : periodos) {
            if (ultimo != null && !periodo.inicio.isAfter(ultimo.fin.plusDays(1 + TOLERANCIA_DIAS_SIN_REGISTRO))) {
                if (periodo.fin.isAfter(ultimo.fin)) {
                    ultimo.fin = periodo.fin;
                }
                if (ORIGEN_CALENDARIO.equals(periodo.origen)) {
                    ultimo.origen = ORIGEN_CALENDARIO;
                }
            } else {
                ultimo = new PeriodoMenstrual(periodo.inicio, periodo.fin, periodo.origen);
                unidos.add(ultimo);
            }
        }
        return unidos;
    }

    private Menstruacion crearMenstruacion(CicloMenstrual ciclo, LocalDate inicio, LocalDate fin) {
        Menstruacion menstruacion = new Menstruacion();
        menstruacion.setFechaInicio(inicio);
        menstruacion.setFechaFin(fin);
        menstruacion.setCicloMenstrual(ciclo);
        menstruacion.setFechaActualizacion(LocalDateTime.now());
        menstruacionRepository.save(menstruacion);
        ciclo.setMenstruacion(menstruacion);
        cicloMenstrualRepository.save(ciclo);
        return menstruacion;
    }

    private void generarPredicciones(Usuaria usuaria, List<CicloMenstrual> ciclosReales) {
        if (ciclosReales.isEmpty()) {
            return;
        }
        CicloMenstrual cicloActual = ciclosReales.get(ciclosReales.size() - 1);
        int duracionCiclo = calcularDuracionPromedioCiclo(usuaria.getIdUsuario());
        int duracionMenstruacion = calcularDuracionPromedioMenstruacion(usuaria.getIdUsuario());

        LocalDate inicioSiguiente = cicloActual.getFechaInicio().plusDays(duracionCiclo);
        LocalDate hoy = LocalDate.now();
        if (!inicioSiguiente.isAfter(hoy)) {
            inicioSiguiente = hoy.plusDays(1);
        }

        for (int i = 0; i < CICLOS_A_PREDECIR; i++) {
            CicloMenstrual cicloEstimado = new CicloMenstrual();
            cicloEstimado.setFechaInicio(inicioSiguiente);
            cicloEstimado.setFechaFin(inicioSiguiente.plusDays(duracionCiclo - 1));
            cicloEstimado.setEstado(CICLO_ESTIMADO);
            cicloEstimado.setOrigen(ORIGEN_PREDICCION);
            cicloEstimado.setUsuaria(usuaria);
            cicloEstimado.setFechaActualizacion(LocalDateTime.now());
            cicloMenstrualRepository.save(cicloEstimado);
            crearMenstruacion(cicloEstimado, inicioSiguiente, inicioSiguiente.plusDays(duracionMenstruacion - 1));

            if (i == 0) {
                recordatorioService.crearRecordatorioMenstruacion(cicloEstimado, DIAS_ANTICIPACION_RECORDATORIO);
            }
            inicioSiguiente = cicloEstimado.getFechaFin().plusDays(1);
        }
    }

    public int calcularDuracionPromedioCiclo(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);
        Integer duracionEncuesta = null;
        if (encuesta != null) {
            duracionEncuesta = encuesta.getDuracionCiclo();
        }
        if (usuaria != null && UsuariaService.METODO_MANUAL.equals(usuaria.getMetodoCalculo()) && duracionEncuesta != null) {
            return duracionEncuesta;
        }

        cicloMenstrualRepository.flush();
        Integer promedio = cicloMenstrualRepository.duracionPromedioCiclo(idUsuario, CICLOS_PARA_PROMEDIO);
        if (promedio != null) {
            return promedio;
        }
        if (duracionEncuesta != null) {
            return duracionEncuesta;
        }
        return DURACION_CICLO_POR_DEFECTO;
    }

    public int calcularDuracionPromedioMenstruacion(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);
        Integer duracionEncuesta = null;
        if (encuesta != null) {
            duracionEncuesta = encuesta.getDuracionMenstruacion();
        }
        if (usuaria != null && UsuariaService.METODO_MANUAL.equals(usuaria.getMetodoCalculo()) && duracionEncuesta != null) {
            return duracionEncuesta;
        }

        cicloMenstrualRepository.flush();
        Integer promedio = cicloMenstrualRepository.duracionPromedioMenstruacion(idUsuario, CICLOS_PARA_PROMEDIO);
        if (promedio != null) {
            return promedio;
        }
        if (duracionEncuesta != null) {
            return duracionEncuesta;
        }
        return DURACION_MENSTRUACION_POR_DEFECTO;
    }

    public CicloActualDTO obtenerCicloActual(Long idUsuario) {
        CicloMenstrual cicloActual = null;
        CicloMenstrual primerEstimado = null;
        for (CicloMenstrual ciclo : cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario)) {
            if (CICLO_ESTIMADO.equals(ciclo.getEstado())) {
                if (primerEstimado == null) {
                    primerEstimado = ciclo;
                }
            } else {
                cicloActual = ciclo;
            }
        }
        if (cicloActual == null) {
            return null;
        }

        LocalDate hoy = LocalDate.now();
        int duracionCiclo = calcularDuracionPromedioCiclo(idUsuario);
        int duracionMenstruacion = calcularDuracionPromedioMenstruacion(idUsuario);
        LocalDate fechaEsperada = cicloActual.getFechaInicio().plusDays(duracionCiclo);
        LocalDate siguientePeriodo = fechaEsperada;
        if (primerEstimado != null) {
            siguientePeriodo = primerEstimado.getFechaInicio();
        }

        CicloActualDTO dto = new CicloActualDTO();
        dto.setIdCicloMenstrual(cicloActual.getIdCicloMenstrual());
        dto.setFechaInicio(cicloActual.getFechaInicio());
        dto.setDiaDelCiclo(diasEntre(cicloActual.getFechaInicio(), hoy) + 1);
        dto.setDuracionEstimada(diasEntre(cicloActual.getFechaInicio(), siguientePeriodo));
        dto.setDuracionEstimadaMenstruacion(duracionMenstruacion);
        dto.setFechaEstimadaSiguientePeriodo(siguientePeriodo);
        dto.setDiasParaSiguientePeriodo(diasEntre(hoy, siguientePeriodo));
        dto.setMetodoCalculo(usuariaRepository.findById(idUsuario).get().getMetodoCalculo());

        boolean retrasado = CICLO_EN_CURSO.equals(cicloActual.getEstado()) && hoy.isAfter(fechaEsperada);
        dto.setPeriodoRetrasado(retrasado);
        if (retrasado) {
            dto.setDiasRetraso(diasEntre(fechaEsperada, hoy));
        } else {
            dto.setDiasRetraso(0);
        }

        LocalDate ovulacion = siguientePeriodo.minusDays(DIAS_OVULACION_ANTES_DEL_PERIODO);
        dto.setFechaEstimadaOvulacion(ovulacion);
        dto.setFase(calcularFase(hoy, cicloActual, ovulacion));
        return dto;
    }

    private String calcularFase(LocalDate fecha, CicloMenstrual ciclo, LocalDate ovulacion) {
        if (ciclo.getMenstruacion() != null && ciclo.getMenstruacion().contieneFecha(fecha)) {
            return "MENSTRUAL";
        }
        if (estaEnVentanaOvulacion(fecha, ovulacion)) {
            return "OVULATORIA";
        }
        if (fecha.isBefore(ovulacion)) {
            return "FOLICULAR";
        }
        return "LUTEA";
    }

    public boolean estaEnVentanaOvulacion(LocalDate fecha, LocalDate ovulacion) {
        return !fecha.isBefore(ovulacion.minusDays(5)) && !fecha.isAfter(ovulacion.plusDays(1));
    }

    public List<CicloMenstrualDTO> listarCiclos(Long idUsuario) {
        List<CicloMenstrualDTO> lista = new ArrayList<CicloMenstrualDTO>();
        for (CicloMenstrual ciclo : cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario)) {
            lista.add(modelMapper.map(ciclo, CicloMenstrualDTO.class));
        }
        return lista;
    }

    public List<MenstruacionDTO> listarProximasMenstruaciones(Long idUsuario) {
        List<MenstruacionDTO> lista = new ArrayList<MenstruacionDTO>();
        for (Menstruacion menstruacion : menstruacionRepository.findByCicloMenstrualUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario)) {
            if (MENSTRUACION_ESTIMADA.equals(menstruacion.getEstado())) {
                lista.add(modelMapper.map(menstruacion, MenstruacionDTO.class));
            }
        }
        return lista;
    }

    public static int diasEntre(LocalDate desde, LocalDate hasta) {
        return (int) ChronoUnit.DAYS.between(desde, hasta);
    }
}
