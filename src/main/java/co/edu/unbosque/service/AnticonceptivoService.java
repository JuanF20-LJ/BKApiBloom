package co.edu.unbosque.service;

import co.edu.unbosque.dto.AnticonceptivoDTO;
import co.edu.unbosque.entity.postgres.Anticonceptivo;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.postgres.AnticonceptivoRepository;
import co.edu.unbosque.repository.postgres.UsuariaRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnticonceptivoService {
    public static final String ESTADO_ACTIVO = "ACTIVO";

    public static final String ESTADO_SUSPENDIDO = "SUSPENDIDO";

    public static final String ESTADO_FINALIZADO = "FINALIZADO";

    @Autowired
    private AnticonceptivoRepository anticonceptivoRepository;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private RecordatorioService recordatorioService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public List<AnticonceptivoDTO> listarAnticonceptivos(Long idUsuario) {
        List<AnticonceptivoDTO> lista = new ArrayList<AnticonceptivoDTO>();
        for (Anticonceptivo anticonceptivo : anticonceptivoRepository.findByUsuariaIdUsuarioOrderByFechaInicioDesc(idUsuario)) {
            lista.add(modelMapper.map(anticonceptivo, AnticonceptivoDTO.class));
        }
        return lista;
    }

    public AnticonceptivoDTO buscarAnticonceptivo(Long idUsuario, Long idAnticonceptivo) {
        Anticonceptivo anticonceptivo = buscarDeUsuaria(idUsuario, idAnticonceptivo);
        if (anticonceptivo == null) {
            return null;
        }
        return modelMapper.map(anticonceptivo, AnticonceptivoDTO.class);
    }

    public int registrarAnticonceptivo(Long idUsuario, AnticonceptivoDTO anticonceptivoDTO) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (!esValido(anticonceptivoDTO)) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Anticonceptivo anticonceptivo = modelMapper.map(anticonceptivoDTO, Anticonceptivo.class);
        anticonceptivo.setIdAnticonceptivo(null);
        completarDatos(anticonceptivo);
        anticonceptivo.setUsuaria(usuaria);
        anticonceptivo.setFechaRegistro(LocalDateTime.now());
        anticonceptivo = anticonceptivoRepository.save(anticonceptivo);

        recordatorioService.actualizarRecordatorioAnticonceptivo(anticonceptivo);
        anticonceptivoDTO.setIdAnticonceptivo(anticonceptivo.getIdAnticonceptivo());
        return CodigoRespuesta.CREADO;
    }

    public int editarAnticonceptivo(Long idUsuario, Long idAnticonceptivo, AnticonceptivoDTO anticonceptivoDTO) {
        Anticonceptivo existente = buscarDeUsuaria(idUsuario, idAnticonceptivo);
        if (existente == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (!esValido(anticonceptivoDTO)) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Anticonceptivo anticonceptivo = modelMapper.map(anticonceptivoDTO, Anticonceptivo.class);
        anticonceptivo.setIdAnticonceptivo(existente.getIdAnticonceptivo());
        completarDatos(anticonceptivo);
        anticonceptivo.setUsuaria(existente.getUsuaria());
        anticonceptivo.setFechaRegistro(existente.getFechaRegistro());
        anticonceptivo = anticonceptivoRepository.save(anticonceptivo);

        recordatorioService.actualizarRecordatorioAnticonceptivo(anticonceptivo);
        return CodigoRespuesta.OK;
    }

    public int eliminarAnticonceptivo(Long idUsuario, Long idAnticonceptivo) {
        Anticonceptivo existente = buscarDeUsuaria(idUsuario, idAnticonceptivo);
        if (existente == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        recordatorioService.eliminarRecordatoriosAnticonceptivo(idUsuario, idAnticonceptivo);
        anticonceptivoRepository.delete(existente);
        return CodigoRespuesta.OK;
    }

    public int registrarAplicacion(Long idUsuario, Long idAnticonceptivo) {
        Anticonceptivo anticonceptivo = buscarDeUsuaria(idUsuario, idAnticonceptivo);
        if (anticonceptivo == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (!ESTADO_ACTIVO.equals(anticonceptivo.getEstado())) {
            return CodigoRespuesta.CONFLICTO;
        }
        if (anticonceptivo.getFrecuenciaDias() == null) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        LocalDate base = LocalDate.now();
        if (anticonceptivo.getProximaAplicacion() != null && anticonceptivo.getProximaAplicacion().isAfter(base)) {
            base = anticonceptivo.getProximaAplicacion();
        }
        anticonceptivo.setProximaAplicacion(base.plusDays(anticonceptivo.getFrecuenciaDias()));
        anticonceptivo = anticonceptivoRepository.save(anticonceptivo);
        recordatorioService.actualizarRecordatorioAnticonceptivo(anticonceptivo);
        return CodigoRespuesta.OK;
    }

    private Anticonceptivo buscarDeUsuaria(Long idUsuario, Long idAnticonceptivo) {
        Anticonceptivo anticonceptivo = anticonceptivoRepository.findById(idAnticonceptivo).orElse(null);
        if (anticonceptivo == null || !anticonceptivo.getUsuaria().getIdUsuario().equals(idUsuario)) {
            return null;
        }
        return anticonceptivo;
    }

    private boolean esValido(AnticonceptivoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty() || dto.getFechaInicio() == null) {
            return false;
        }
        if (dto.getProximaAplicacion() == null) {
            return false;
        }
        if (dto.getEstado() != null && !ESTADO_ACTIVO.equals(dto.getEstado()) && !ESTADO_SUSPENDIDO.equals(dto.getEstado())
                && !ESTADO_FINALIZADO.equals(dto.getEstado())) {
            return false;
        }
        if (dto.getFechaFin() != null && dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            return false;
        }
        if (dto.getProximaAplicacion().isBefore(dto.getFechaInicio())) {
            return false;
        }
        if (dto.getFrecuenciaDias() != null && dto.getFrecuenciaDias() <= 0) {
            return false;
        }
        return true;
    }

    private void completarDatos(Anticonceptivo anticonceptivo) {
        if (anticonceptivo.getEstado() == null) {
            anticonceptivo.setEstado(ESTADO_ACTIVO);
        }
        if (ESTADO_FINALIZADO.equals(anticonceptivo.getEstado())) {
            if (anticonceptivo.getFechaFin() == null) {
                anticonceptivo.setFechaFin(LocalDate.now());
            }
        }
    }
}
