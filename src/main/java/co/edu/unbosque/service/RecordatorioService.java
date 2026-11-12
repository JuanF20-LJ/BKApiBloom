package co.edu.unbosque.service;

import co.edu.unbosque.dto.RecordatorioDTO;
import co.edu.unbosque.entity.postgres.Anticonceptivo;
import co.edu.unbosque.entity.postgres.CicloMenstrual;
import co.edu.unbosque.entity.postgres.Recordatorio;
import co.edu.unbosque.entity.postgres.RecordatorioAnticonceptivo;
import co.edu.unbosque.entity.postgres.RecordatorioMenstruacion;
import co.edu.unbosque.repository.postgres.RecordatorioAnticonceptivoRepository;
import co.edu.unbosque.repository.postgres.RecordatorioMenstruacionRepository;
import co.edu.unbosque.repository.postgres.RecordatorioRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class RecordatorioService {
    private static final int HORA_RECORDATORIO = 8;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Autowired
    private RecordatorioMenstruacionRepository recordatorioMenstruacionRepository;

    @Autowired
    private RecordatorioAnticonceptivoRepository recordatorioAnticonceptivoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<RecordatorioMenstruacion, RecordatorioDTO>() {
            @Override
            protected void configure() {
                map().setIdCicloMenstrual(source.getCicloMenstrual().getIdCicloMenstrual());
            }
        });
        modelMapper.addMappings(new PropertyMap<RecordatorioAnticonceptivo, RecordatorioDTO>() {
            @Override
            protected void configure() {
                map().setIdAnticonceptivo(source.getAnticonceptivo().getIdAnticonceptivo());
            }
        });
    }

    public List<RecordatorioDTO> listarRecordatorios(Long idUsuario, boolean soloPendientes) {
        List<RecordatorioDTO> lista = new ArrayList<RecordatorioDTO>();
        for (Recordatorio recordatorio : buscarRecordatoriosDeUsuaria(idUsuario)) {
            if (soloPendientes && Boolean.TRUE.equals(recordatorio.getLeido())) {
                continue;
            }

            lista.add(modelMapper.map(recordatorio, RecordatorioDTO.class));
        }
        return lista;
    }

    private List<Recordatorio> buscarRecordatoriosDeUsuaria(Long idUsuario) {
        List<Recordatorio> todos = new ArrayList<Recordatorio>();
        todos.addAll(recordatorioMenstruacionRepository.findByCicloMenstrualUsuariaIdUsuario(idUsuario));
        todos.addAll(recordatorioAnticonceptivoRepository.findByAnticonceptivoUsuariaIdUsuario(idUsuario));
        Collections.sort(todos, new Comparator<Recordatorio>() {
            @Override
            public int compare(Recordatorio a, Recordatorio b) {
                return a.getFechaProgramado().compareTo(b.getFechaProgramado());
            }
        });
        return todos;
    }

    public int marcarComoLeido(Long idUsuario, Long idRecordatorio) {
        Recordatorio recordatorio = buscarDeUsuaria(idUsuario, idRecordatorio);
        if (recordatorio == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        recordatorio.setLeido(Boolean.TRUE);
        recordatorioRepository.save(recordatorio);
        return CodigoRespuesta.OK;
    }

    public int eliminarRecordatorio(Long idUsuario, Long idRecordatorio) {
        Recordatorio recordatorio = buscarDeUsuaria(idUsuario, idRecordatorio);
        if (recordatorio == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        recordatorioRepository.delete(recordatorio);
        return CodigoRespuesta.OK;
    }

    private Recordatorio buscarDeUsuaria(Long idUsuario, Long idRecordatorio) {
        Recordatorio recordatorio = recordatorioRepository.findById(idRecordatorio).orElse(null);

        if (recordatorio == null || !recordatorio.obtenerIdUsuario().equals(idUsuario)) {
            return null;
        }
        return recordatorio;
    }

    public void crearRecordatorioMenstruacion(CicloMenstrual cicloEstimado, int diasAnticipacion) {
        LocalDateTime fechaProgramado = cicloEstimado.getFechaInicio().minusDays(diasAnticipacion).atTime(HORA_RECORDATORIO, 0);
        if (fechaProgramado.isBefore(LocalDateTime.now())) {
            fechaProgramado = LocalDateTime.now();
        }
        RecordatorioMenstruacion recordatorio = new RecordatorioMenstruacion();
        recordatorio.setMensaje("Tu próxima menstruación se estima para el " + cicloEstimado.getFechaInicio().format(FORMATO_FECHA) + ".");
        recordatorio.setFechaProgramado(fechaProgramado);
        recordatorio.setFechaCreacion(LocalDateTime.now());
        recordatorio.setLeido(Boolean.FALSE);
        recordatorio.setDiasAnticipacion(diasAnticipacion);
        recordatorio.setCicloMenstrual(cicloEstimado);
        recordatorioRepository.save(recordatorio);
    }

    public void eliminarRecordatoriosMenstruacion(Long idUsuario) {
        recordatorioMenstruacionRepository.deleteAll(recordatorioMenstruacionRepository.findByCicloMenstrualUsuariaIdUsuario(idUsuario));
    }

    public void actualizarRecordatorioAnticonceptivo(Anticonceptivo anticonceptivo) {
        LocalDateTime ahora = LocalDateTime.now();
        for (RecordatorioAnticonceptivo delMetodo : recordatorioAnticonceptivoRepository.findByAnticonceptivoIdAnticonceptivo(anticonceptivo.getIdAnticonceptivo())) {
            boolean estaPendiente = !Boolean.TRUE.equals(delMetodo.getLeido()) || delMetodo.getFechaProgramado().isAfter(ahora);
            if (estaPendiente) {
                recordatorioAnticonceptivoRepository.delete(delMetodo);
            }
        }

        boolean activo = AnticonceptivoService.ESTADO_ACTIVO.equals(anticonceptivo.getEstado());
        LocalDate proxima = anticonceptivo.getProximaAplicacion();
        if (!activo || proxima == null || proxima.isBefore(LocalDate.now())) {
            return;
        }
        RecordatorioAnticonceptivo recordatorio = new RecordatorioAnticonceptivo();
        recordatorio.setMensaje("Recuerda tu método anticonceptivo: " + anticonceptivo.getNombre() + " (" + proxima.format(FORMATO_FECHA) + ").");
        recordatorio.setFechaProgramado(proxima.atTime(HORA_RECORDATORIO, 0));
        recordatorio.setFechaCreacion(ahora);
        recordatorio.setLeido(Boolean.FALSE);
        recordatorio.setFechaAplicacion(proxima);
        recordatorio.setAnticonceptivo(anticonceptivo);
        recordatorioRepository.save(recordatorio);
    }

    public void eliminarRecordatoriosAnticonceptivo(Long idUsuario, Long idAnticonceptivo) {
        recordatorioAnticonceptivoRepository.deleteAll(recordatorioAnticonceptivoRepository.findByAnticonceptivoIdAnticonceptivo(idAnticonceptivo));
    }

    public void eliminarRecordatoriosDeUsuaria(Long idUsuario) {
        recordatorioMenstruacionRepository.deleteAll(recordatorioMenstruacionRepository.findByCicloMenstrualUsuariaIdUsuario(idUsuario));
        recordatorioAnticonceptivoRepository.deleteAll(recordatorioAnticonceptivoRepository.findByAnticonceptivoUsuariaIdUsuario(idUsuario));
    }
}
