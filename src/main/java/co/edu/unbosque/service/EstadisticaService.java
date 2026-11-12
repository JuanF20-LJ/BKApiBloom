package co.edu.unbosque.service;

import co.edu.unbosque.dto.CicloResumenDTO;
import co.edu.unbosque.dto.EstadisticaPeriodoDTO;
import co.edu.unbosque.dto.EstadisticaResumenDTO;
import co.edu.unbosque.dto.SintomaConteoDTO;
import co.edu.unbosque.entity.mongo.CicloResumen;
import co.edu.unbosque.entity.mongo.EstadisticaMensual;
import co.edu.unbosque.entity.mongo.EstadisticaResumen;
import co.edu.unbosque.entity.mongo.SintomaConteo;
import co.edu.unbosque.entity.postgres.CicloMenstrual;
import co.edu.unbosque.entity.postgres.Menstruacion;
import co.edu.unbosque.entity.postgres.RegistroDiario;
import co.edu.unbosque.entity.postgres.Sintoma;
import co.edu.unbosque.repository.mongo.EstadisticaMensualRepository;
import co.edu.unbosque.repository.mongo.EstadisticaResumenRepository;
import co.edu.unbosque.repository.postgres.CicloMenstrualRepository;
import co.edu.unbosque.repository.postgres.MenstruacionRepository;
import co.edu.unbosque.repository.postgres.RegistroDiarioRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadisticaService {
    private static final int TOP_SINTOMAS = 5;

    private static final int VERSION_ESQUEMA = 1;

    @Autowired
    private EstadisticaResumenRepository estadisticaResumenRepository;

    @Autowired
    private EstadisticaMensualRepository estadisticaMensualRepository;

    @Autowired
    private CicloMenstrualRepository cicloMenstrualRepository;

    @Autowired
    private MenstruacionRepository menstruacionRepository;

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public EstadisticaResumenDTO verEstadisticas(Long idUsuario) {
        EstadisticaResumen resumen = estadisticaResumenRepository.findById(idUsuario).orElse(null);
        if (resumen == null) {
            recalcularEstadisticas(idUsuario);
            resumen = estadisticaResumenRepository.findById(idUsuario).orElse(null);
        }
        return modelMapper.map(resumen, EstadisticaResumenDTO.class);
    }

    public EstadisticaPeriodoDTO verEstadisticasPeriodo(Long idUsuario, String desde, String hasta) {
        if (!esMesValido(desde) || !esMesValido(hasta)) {
            return null;
        }
        int claveDesde = convertirMesEnNumero(desde);
        int claveHasta = convertirMesEnNumero(hasta);
        if (claveDesde > claveHasta) {
            return null;
        }

        int diasRegistrados = 0;
        int diasMenstruacion = 0;
        List<CicloResumen> ciclos = new ArrayList<CicloResumen>();
        List<Integer> duracionesPeriodo = new ArrayList<Integer>();
        List<SintomaConteo> sintomas = new ArrayList<SintomaConteo>();

        for (EstadisticaMensual bucket : estadisticaMensualRepository.findByIdUsuarioOrderByAnioAscMesAsc(idUsuario)) {
            int claveBucket = bucket.getAnio() * 100 + bucket.getMes();
            if (claveBucket < claveDesde || claveBucket > claveHasta) {
                continue;
            }
            diasRegistrados = diasRegistrados + bucket.getDiasRegistrados();
            diasMenstruacion = diasMenstruacion + bucket.getDiasMenstruacion();
            ciclos.addAll(bucket.getCiclosFinalizados());
            duracionesPeriodo.addAll(bucket.getDuracionesPeriodo());
            for (SintomaConteo conteo : bucket.getSintomas()) {
                sumarSintoma(sintomas, conteo.getIdSintoma(), conteo.getNombre(), conteo.getConteo());
            }
        }

        EstadisticaPeriodoDTO dto = new EstadisticaPeriodoDTO();
        dto.setDesde(desde);
        dto.setHasta(hasta);
        dto.setDiasRegistrados(diasRegistrados);
        dto.setDiasMenstruacion(diasMenstruacion);
        dto.setTotalCiclos(ciclos.size());
        dto.setPromedioDuracionCiclo(promediarCiclos(ciclos));
        dto.setPromedioDuracionPeriodo(promediar(duracionesPeriodo));
        CicloResumen corto = buscarCicloExtremo(ciclos, true);
        CicloResumen largo = buscarCicloExtremo(ciclos, false);
        if (corto != null) {
            dto.setCicloMasCorto(modelMapper.map(corto, CicloResumenDTO.class));
        }
        if (largo != null) {
            dto.setCicloMasLargo(modelMapper.map(largo, CicloResumenDTO.class));
        }
        for (SintomaConteo conteo : obtenerTop(sintomas)) {
            dto.getSintomasFrecuentes().add(modelMapper.map(conteo, SintomaConteoDTO.class));
        }
        return dto;
    }

    public void recalcularEstadisticas(Long idUsuario) {
        List<CicloMenstrual> ciclosFinalizados = new ArrayList<CicloMenstrual>();
        for (CicloMenstrual ciclo : cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario)) {
            if (CicloMenstrualService.CICLO_FINALIZADO.equals(ciclo.getEstado())) {
                ciclosFinalizados.add(ciclo);
            }
        }
        List<Menstruacion> menstruacionesConfirmadas = new ArrayList<Menstruacion>();
        for (Menstruacion menstruacion : menstruacionRepository.findByCicloMenstrualUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario)) {
            if (CicloMenstrualService.MENSTRUACION_CONFIRMADA.equals(menstruacion.getEstado()) && menstruacion.getFechaFin() != null) {
                menstruacionesConfirmadas.add(menstruacion);
            }
        }
        List<RegistroDiario> registros = registroDiarioRepository.findByUsuariaIdUsuarioOrderByFechaAsc(idUsuario);

        guardarResumen(idUsuario, ciclosFinalizados, menstruacionesConfirmadas, registros);
        guardarBucketsMensuales(idUsuario, ciclosFinalizados, menstruacionesConfirmadas, registros);
    }

    public void eliminarEstadisticas(Long idUsuario) {
        estadisticaResumenRepository.deleteById(idUsuario);
        estadisticaMensualRepository.deleteByIdUsuario(idUsuario);
    }

    private void guardarResumen(Long idUsuario, List<CicloMenstrual> ciclos, List<Menstruacion> menstruaciones, List<RegistroDiario> registros) {
        List<CicloResumen> resumenCiclos = new ArrayList<CicloResumen>();
        for (CicloMenstrual ciclo : ciclos) {
            resumenCiclos.add(crearCicloResumen(ciclo));
        }
        List<Integer> duracionesPeriodo = new ArrayList<Integer>();
        for (Menstruacion menstruacion : menstruaciones) {
            duracionesPeriodo.add(menstruacion.getDuracion());
        }
        List<SintomaConteo> sintomas = new ArrayList<SintomaConteo>();
        for (RegistroDiario registro : registros) {
            for (Sintoma sintoma : registro.getSintomas()) {
                sumarSintoma(sintomas, sintoma.getIdSintoma(), sintoma.getNombre(), 1);
            }
        }

        EstadisticaResumen resumen = new EstadisticaResumen();
        resumen.setIdUsuario(idUsuario);
        resumen.setSchemaVersion(VERSION_ESQUEMA);
        resumen.setFechaCalculo(LocalDateTime.now());
        resumen.setTotalCiclos(resumenCiclos.size());
        resumen.setPromedioDuracionCiclo(promediarCiclos(resumenCiclos));
        resumen.setPromedioDuracionPeriodo(promediar(duracionesPeriodo));
        resumen.setCicloMasCorto(buscarCicloExtremo(resumenCiclos, true));
        resumen.setCicloMasLargo(buscarCicloExtremo(resumenCiclos, false));
        resumen.setSintomasFrecuentes(obtenerTop(sintomas));
        resumen.setTotalDiasRegistrados(registros.size());
        estadisticaResumenRepository.save(resumen);
    }

    private void guardarBucketsMensuales(Long idUsuario, List<CicloMenstrual> ciclos, List<Menstruacion> menstruaciones, List<RegistroDiario> registros) {
        estadisticaMensualRepository.deleteByIdUsuario(idUsuario);
        Map<String, EstadisticaMensual> buckets = new HashMap<String, EstadisticaMensual>();

        for (RegistroDiario registro : registros) {
            EstadisticaMensual bucket = obtenerBucket(buckets, idUsuario, registro.getFecha());
            bucket.setDiasRegistrados(bucket.getDiasRegistrados() + 1);
            if (Boolean.TRUE.equals(registro.getHayMenstruacion())) {
                bucket.setDiasMenstruacion(bucket.getDiasMenstruacion() + 1);
            }
            for (Sintoma sintoma : registro.getSintomas()) {
                sumarSintoma(bucket.getSintomas(), sintoma.getIdSintoma(), sintoma.getNombre(), 1);
            }
        }

        for (CicloMenstrual ciclo : ciclos) {
            obtenerBucket(buckets, idUsuario, ciclo.getFechaFin()).getCiclosFinalizados().add(crearCicloResumen(ciclo));
        }
        for (Menstruacion menstruacion : menstruaciones) {
            obtenerBucket(buckets, idUsuario, menstruacion.getFechaFin()).getDuracionesPeriodo().add(menstruacion.getDuracion());
        }
        for (EstadisticaMensual bucket : buckets.values()) {
            estadisticaMensualRepository.save(bucket);
        }
    }

    private EstadisticaMensual obtenerBucket(Map<String, EstadisticaMensual> buckets, Long idUsuario, LocalDate fecha) {
        String id = idUsuario + "_" + fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue());
        EstadisticaMensual bucket = buckets.get(id);
        if (bucket == null) {
            bucket = new EstadisticaMensual();
            bucket.setIdEstadisticaMensual(id);
            bucket.setSchemaVersion(VERSION_ESQUEMA);
            bucket.setIdUsuario(idUsuario);
            bucket.setAnio(fecha.getYear());
            bucket.setMes(fecha.getMonthValue());
            bucket.setDiasRegistrados(0);
            bucket.setDiasMenstruacion(0);
            bucket.setFechaCalculo(LocalDateTime.now());
            buckets.put(id, bucket);
        }
        return bucket;
    }

    private CicloResumen crearCicloResumen(CicloMenstrual ciclo) {
        CicloResumen resumen = new CicloResumen();
        resumen.setIdCicloMenstrual(ciclo.getIdCicloMenstrual());
        resumen.setFechaInicio(ciclo.getFechaInicio());
        resumen.setDuracion(ciclo.getDuracion());
        return resumen;
    }

    private void sumarSintoma(List<SintomaConteo> conteos, Long idSintoma, String nombre, int cantidad) {
        for (SintomaConteo conteo : conteos) {
            if (conteo.getIdSintoma().equals(idSintoma)) {
                conteo.setConteo(conteo.getConteo() + cantidad);
                return;
            }
        }
        SintomaConteo nuevo = new SintomaConteo();
        nuevo.setIdSintoma(idSintoma);
        nuevo.setNombre(nombre);
        nuevo.setConteo(cantidad);
        conteos.add(nuevo);
    }

    private List<SintomaConteo> obtenerTop(List<SintomaConteo> conteos) {
        List<SintomaConteo> ordenados = new ArrayList<SintomaConteo>(conteos);
        Collections.sort(ordenados, new Comparator<SintomaConteo>() {
            @Override
            public int compare(SintomaConteo a, SintomaConteo b) {
                return b.getConteo().compareTo(a.getConteo());
            }
        });
        List<SintomaConteo> top = new ArrayList<SintomaConteo>();
        for (SintomaConteo conteo : ordenados) {
            if (top.size() < TOP_SINTOMAS) {
                top.add(conteo);
            }
        }
        return top;
    }

    private CicloResumen buscarCicloExtremo(List<CicloResumen> ciclos, boolean corto) {
        CicloResumen extremo = null;
        for (CicloResumen ciclo : ciclos) {
            if (extremo == null) {
                extremo = ciclo;
            } else if (corto && ciclo.getDuracion() < extremo.getDuracion()) {
                extremo = ciclo;
            } else if (!corto && ciclo.getDuracion() > extremo.getDuracion()) {
                extremo = ciclo;
            }
        }
        return extremo;
    }

    private Double promediarCiclos(List<CicloResumen> ciclos) {
        List<Integer> duraciones = new ArrayList<Integer>();
        for (CicloResumen ciclo : ciclos) {
            duraciones.add(ciclo.getDuracion());
        }
        return promediar(duraciones);
    }

    private Double promediar(List<Integer> valores) {
        if (valores.isEmpty()) {
            return null;
        }
        double suma = 0;
        for (Integer valor : valores) {
            suma = suma + valor;
        }
        return Math.round((suma / valores.size()) * 10.0) / 10.0;
    }

    private boolean esMesValido(String texto) {
        if (texto == null || !texto.matches("\\d{4}-\\d{2}")) {
            return false;
        }
        int mes = Integer.parseInt(texto.substring(5, 7));
        return mes >= 1 && mes <= 12;
    }

    private int convertirMesEnNumero(String texto) {
        return Integer.parseInt(texto.substring(0, 4)) * 100 + Integer.parseInt(texto.substring(5, 7));
    }
}
