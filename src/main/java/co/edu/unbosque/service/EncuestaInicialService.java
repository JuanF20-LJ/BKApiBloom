package co.edu.unbosque.service;

import co.edu.unbosque.dto.CicloEncuestaDTO;
import co.edu.unbosque.dto.EncuestaInicialDTO;
import co.edu.unbosque.entity.postgres.EncuestaInicial;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.postgres.EncuestaInicialRepository;
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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EncuestaInicialService {
    public static final int CICLO_MINIMO = 15;

    public static final int CICLO_MAXIMO = 60;

    public static final int MENSTRUACION_MINIMA = 1;

    public static final int MENSTRUACION_MAXIMA = 15;

    @Autowired
    private EncuestaInicialRepository encuestaInicialRepository;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private CicloMenstrualService cicloMenstrualService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public int llenarEncuesta(Long idUsuario, EncuestaInicialDTO encuestaDTO) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (encuestaInicialRepository.existsByUsuariaIdUsuario(idUsuario)) {
            return CodigoRespuesta.CONFLICTO;
        }
        if (encuestaDTO.getIngresoRango() == null) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        if (!esDatoSaludValido(encuestaDTO.getAltura(), encuestaDTO.getPeso(), encuestaDTO.getHorasSueno(), true)) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        List<CicloEncuestaDTO> ciclos = new ArrayList<CicloEncuestaDTO>();
        if (encuestaDTO.getIngresoRango()) {
            if (!sonDuracionesValidas(encuestaDTO.getDuracionCiclo(), encuestaDTO.getDuracionMenstruacion(), true)) {
                return CodigoRespuesta.SOLICITUD_INCORRECTA;
            }
        } else {
            if (encuestaDTO.getCiclosIniciales() != null) {
                ciclos = new ArrayList<CicloEncuestaDTO>(encuestaDTO.getCiclosIniciales());
            }
            if (!sonCiclosValidos(ciclos)) {
                return CodigoRespuesta.SOLICITUD_INCORRECTA;
            }
            if (!sonDuracionesValidas(encuestaDTO.getDuracionCiclo(), encuestaDTO.getDuracionMenstruacion(), false)) {
                return CodigoRespuesta.SOLICITUD_INCORRECTA;
            }
        }

        EncuestaInicial encuesta = modelMapper.map(encuestaDTO, EncuestaInicial.class);
        encuesta.setIdEncuestaInicial(null);
        encuesta.setFechaRealizacion(LocalDateTime.now());
        encuesta.setCantidadCiclosIngresados(ciclos.size());
        encuesta.setUsuaria(usuaria);
        encuestaInicialRepository.save(encuesta);

        modelMapper.map(encuestaDTO, usuaria);
        if (encuestaDTO.getIngresoRango()) {
            usuaria.setMetodoCalculo(UsuariaService.METODO_MANUAL);
        } else {
            usuaria.setMetodoCalculo(UsuariaService.METODO_AUTOMATICO);
        }

        usuariaRepository.save(usuaria);

        for (CicloEncuestaDTO ciclo : ciclos) {
            cicloMenstrualService.crearCicloDeEncuesta(usuaria, ciclo.getFechaInicioMenstruacion(), ciclo.getFechaFinMenstruacion());
        }
        cicloMenstrualService.recalcularCiclos(idUsuario);
        return CodigoRespuesta.CREADO;
    }

    public EncuestaInicialDTO buscarEncuesta(Long idUsuario) {
        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);
        if (encuesta == null) {
            return null;
        }
        EncuestaInicialDTO encuestaDTO = modelMapper.map(encuesta, EncuestaInicialDTO.class);
        modelMapper.map(encuesta.getUsuaria(), encuestaDTO);
        return encuestaDTO;
    }

    private boolean sonCiclosValidos(List<CicloEncuestaDTO> ciclos) {
        if (ciclos.size() < 1 || ciclos.size() > 3) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        for (CicloEncuestaDTO ciclo : ciclos) {
            if (ciclo.getFechaInicioMenstruacion() == null || ciclo.getFechaFinMenstruacion() == null) {
                return false;
            }
            if (ciclo.getFechaFinMenstruacion().isBefore(ciclo.getFechaInicioMenstruacion())) {
                return false;
            }
            if (ciclo.getFechaFinMenstruacion().isAfter(hoy)) {
                return false;
            }
            long dias = ChronoUnit.DAYS.between(ciclo.getFechaInicioMenstruacion(), ciclo.getFechaFinMenstruacion()) + 1;
            if (dias > MENSTRUACION_MAXIMA) {
                return false;
            }
        }

        Collections.sort(ciclos, new Comparator<CicloEncuestaDTO>() {
            @Override
            public int compare(CicloEncuestaDTO a, CicloEncuestaDTO b) {
                return a.getFechaInicioMenstruacion().compareTo(b.getFechaInicioMenstruacion());
            }
        });
        CicloEncuestaDTO anterior = null;
        for (CicloEncuestaDTO ciclo : ciclos) {
            if (anterior != null && !ciclo.getFechaInicioMenstruacion().isAfter(anterior.getFechaFinMenstruacion())) {
                return false;
            }
            anterior = ciclo;
        }
        return true;
    }

    public static boolean esDatoSaludValido(Double altura, Double peso, Double horasSueno, boolean obligatorio) {
        if (obligatorio && (altura == null || peso == null || horasSueno == null)) {
            return false;
        }
        if (altura != null && (altura < 0.5 || altura > 2.5)) {
            return false;
        }
        if (peso != null && (peso < 20 || peso > 300)) {
            return false;
        }
        if (horasSueno != null && (horasSueno < 0 || horasSueno > 24)) {
            return false;
        }
        return true;
    }

    public static boolean sonDuracionesValidas(Integer duracionCiclo, Integer duracionMenstruacion, boolean obligatorio) {
        if (obligatorio && (duracionCiclo == null || duracionMenstruacion == null)) {
            return false;
        }
        if (duracionCiclo != null && (duracionCiclo < CICLO_MINIMO || duracionCiclo > CICLO_MAXIMO)) {
            return false;
        }
        if (duracionMenstruacion != null && (duracionMenstruacion < MENSTRUACION_MINIMA || duracionMenstruacion > MENSTRUACION_MAXIMA)) {
            return false;
        }
        if (duracionCiclo != null && duracionMenstruacion != null && duracionMenstruacion >= duracionCiclo) {
            return false;
        }
        return true;
    }
}
