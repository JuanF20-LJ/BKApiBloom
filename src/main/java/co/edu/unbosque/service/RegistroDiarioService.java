package co.edu.unbosque.service;

import co.edu.unbosque.dto.ActividadSexualDTO;
import co.edu.unbosque.dto.EmocionDTO;
import co.edu.unbosque.dto.RegistroDiarioDTO;
import co.edu.unbosque.dto.SintomaDTO;
import co.edu.unbosque.entity.postgres.ActividadSexual;
import co.edu.unbosque.entity.postgres.Emocion;
import co.edu.unbosque.entity.postgres.RegistroDiario;
import co.edu.unbosque.entity.postgres.Sintoma;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.postgres.ActividadSexualRepository;
import co.edu.unbosque.repository.postgres.EmocionRepository;
import co.edu.unbosque.repository.postgres.RegistroDiarioRepository;
import co.edu.unbosque.repository.postgres.SintomaRepository;
import co.edu.unbosque.repository.postgres.UsuariaRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistroDiarioService {
    private static final String[] TIPOS_FLUJO = {"MANCHADO", "LIGERO", "MEDIO", "ABUNDANTE"};

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private SintomaRepository sintomaRepository;

    @Autowired
    private EmocionRepository emocionRepository;

    @Autowired
    private ActividadSexualRepository actividadSexualRepository;

    @Autowired
    private CicloMenstrualService cicloMenstrualService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<RegistroDiario, RegistroDiarioDTO>() {
            @Override
            protected void configure() {
                map().setIdCicloMenstrual(source.getCicloMenstrual().getIdCicloMenstrual());
                map().setIdMenstruacion(source.getMenstruacion().getIdMenstruacion());
            }
        });
    }

    public int registrarDia(Long idUsuario, RegistroDiarioDTO registroDTO) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        LocalDate fecha = registroDTO.getFecha();
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        if (fecha.isAfter(LocalDate.now())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        if (registroDiarioRepository.findByUsuariaIdUsuarioAndFecha(idUsuario, fecha).isPresent()) {
            return CodigoRespuesta.CONFLICTO;
        }

        RegistroDiario registro = construirRegistro(registroDTO);
        if (registro == null) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        registro.setIdRegistroDiario(null);
        registro.setFecha(fecha);
        registro.setUsuaria(usuaria);
        registro.setFechaCreacion(LocalDateTime.now());
        registro.setFechaModificacion(null);
        registroDiarioRepository.save(registro);

        cicloMenstrualService.recalcularCiclos(idUsuario);
        return CodigoRespuesta.CREADO;
    }

    public int editarDia(Long idUsuario, LocalDate fecha, RegistroDiarioDTO registroDTO) {
        RegistroDiario existente = registroDiarioRepository.findByUsuariaIdUsuarioAndFecha(idUsuario, fecha).orElse(null);
        if (existente == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        RegistroDiario registro = construirRegistro(registroDTO);
        if (registro == null) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        registro.setIdRegistroDiario(existente.getIdRegistroDiario());
        registro.setFecha(existente.getFecha());
        registro.setUsuaria(existente.getUsuaria());
        registro.setFechaCreacion(existente.getFechaCreacion());
        registro.setFechaModificacion(LocalDateTime.now());
        registroDiarioRepository.save(registro);

        cicloMenstrualService.recalcularCiclos(idUsuario);
        return CodigoRespuesta.OK;
    }

    public int eliminarDia(Long idUsuario, LocalDate fecha) {
        RegistroDiario existente = registroDiarioRepository.findByUsuariaIdUsuarioAndFecha(idUsuario, fecha).orElse(null);
        if (existente == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        registroDiarioRepository.delete(existente);
        cicloMenstrualService.recalcularCiclos(idUsuario);
        return CodigoRespuesta.OK;
    }

    public RegistroDiarioDTO buscarDia(Long idUsuario, LocalDate fecha) {
        RegistroDiario registro = registroDiarioRepository.findByUsuariaIdUsuarioAndFecha(idUsuario, fecha).orElse(null);
        if (registro == null) {
            return null;
        }
        return modelMapper.map(registro, RegistroDiarioDTO.class);
    }

    public List<RegistroDiarioDTO> listarDias(Long idUsuario, LocalDate desde, LocalDate hasta) {
        List<RegistroDiario> registros;
        if (desde == null || hasta == null) {
            registros = registroDiarioRepository.findByUsuariaIdUsuarioOrderByFechaAsc(idUsuario);
        } else {
            registros = registroDiarioRepository.findByUsuariaIdUsuarioAndFechaBetweenOrderByFechaAsc(idUsuario, desde, hasta);
        }
        List<RegistroDiarioDTO> lista = new ArrayList<RegistroDiarioDTO>();
        for (RegistroDiario registro : registros) {
            lista.add(modelMapper.map(registro, RegistroDiarioDTO.class));
        }
        return lista;
    }

    private RegistroDiario construirRegistro(RegistroDiarioDTO registroDTO) {
        if (registroDTO.getTipoFlujo() != null && !esTipoFlujoValido(registroDTO.getTipoFlujo())) {
            return null;
        }
        RegistroDiario registro = modelMapper.map(registroDTO, RegistroDiario.class);

        if (registro.getHayMenstruacion() == null) {
            registro.setHayMenstruacion(Boolean.FALSE);
        }

        if (!registro.getHayMenstruacion()) {
            registro.setTipoFlujo(null);
        }

        List<Sintoma> sintomas = buscarSintomas(registroDTO.getSintomas());
        List<Emocion> emociones = buscarEmociones(registroDTO.getEmociones());
        List<ActividadSexual> actividades = buscarActividadesSexuales(registroDTO.getActividadesSexuales());
        if (sintomas == null || emociones == null || actividades == null) {
            return null;
        }
        registro.setSintomas(sintomas);
        registro.setEmociones(emociones);
        registro.setActividadesSexuales(actividades);
        registro.setCicloMenstrual(null);
        registro.setMenstruacion(null);
        return registro;
    }

    private boolean esTipoFlujoValido(String tipoFlujo) {
        for (String tipo : TIPOS_FLUJO) {
            if (tipo.equals(tipoFlujo)) {
                return true;
            }
        }
        return false;
    }

    private List<Sintoma> buscarSintomas(List<SintomaDTO> sintomasDTO) {
        List<Sintoma> resultado = new ArrayList<Sintoma>();
        if (sintomasDTO == null) {
            return resultado;
        }
        for (SintomaDTO sintomaDTO : sintomasDTO) {
            if (sintomaDTO.getIdSintoma() == null) {
                return null;
            }
            Sintoma sintoma = sintomaRepository.findById(sintomaDTO.getIdSintoma()).orElse(null);
            if (sintoma == null) {
                return null;
            }
            if (!resultado.contains(sintoma)) {
                resultado.add(sintoma);
            }
        }
        return resultado;
    }

    private List<Emocion> buscarEmociones(List<EmocionDTO> emocionesDTO) {
        List<Emocion> resultado = new ArrayList<Emocion>();
        if (emocionesDTO == null) {
            return resultado;
        }
        for (EmocionDTO emocionDTO : emocionesDTO) {
            if (emocionDTO.getIdEmocion() == null) {
                return null;
            }
            Emocion emocion = emocionRepository.findById(emocionDTO.getIdEmocion()).orElse(null);
            if (emocion == null) {
                return null;
            }
            if (!resultado.contains(emocion)) {
                resultado.add(emocion);
            }
        }
        return resultado;
    }

    private List<ActividadSexual> buscarActividadesSexuales(List<ActividadSexualDTO> actividadesDTO) {
        List<ActividadSexual> resultado = new ArrayList<ActividadSexual>();
        if (actividadesDTO == null) {
            return resultado;
        }
        for (ActividadSexualDTO actividadDTO : actividadesDTO) {
            if (actividadDTO.getIdActividadSexual() == null) {
                return null;
            }
            ActividadSexual actividad = actividadSexualRepository.findById(actividadDTO.getIdActividadSexual()).orElse(null);
            if (actividad == null) {
                return null;
            }
            if (!resultado.contains(actividad)) {
                resultado.add(actividad);
            }
        }
        return resultado;
    }
}
