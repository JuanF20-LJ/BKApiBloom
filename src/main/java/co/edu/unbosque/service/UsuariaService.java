package co.edu.unbosque.service;

import co.edu.unbosque.correos.CorreoAuxiliar;
import co.edu.unbosque.dto.ActualizarAjustesDTO;
import co.edu.unbosque.dto.AjustesDTO;
import co.edu.unbosque.dto.CambioContraseniaDTO;
import co.edu.unbosque.dto.InicioSesionDTO;
import co.edu.unbosque.dto.RegistroUsuariaDTO;
import co.edu.unbosque.dto.SolicitudCodigoDTO;
import co.edu.unbosque.dto.UsuariaDTO;
import co.edu.unbosque.dto.VerificacionCodigoDTO;
import co.edu.unbosque.entity.postgres.Anticonceptivo;
import co.edu.unbosque.entity.postgres.CicloMenstrual;
import co.edu.unbosque.entity.postgres.CodigoVerificacion;
import co.edu.unbosque.entity.postgres.EncuestaInicial;
import co.edu.unbosque.entity.postgres.Menstruacion;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.postgres.AnticonceptivoRepository;
import co.edu.unbosque.repository.postgres.CicloMenstrualRepository;
import co.edu.unbosque.repository.postgres.CodigoVerificacionRepository;
import co.edu.unbosque.repository.postgres.EncuestaInicialRepository;
import co.edu.unbosque.repository.postgres.MenstruacionRepository;
import co.edu.unbosque.repository.postgres.RegistroDiarioRepository;
import co.edu.unbosque.repository.postgres.UsuariaRepository;
import co.edu.unbosque.seguridad.HashContrasenia;
import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuariaService {
    public static final String METODO_AUTOMATICO = "AUTOMATICO";

    public static final String METODO_MANUAL = "MANUAL";

    private static final int LARGO_MINIMO_CONTRASENIA = 8;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private CodigoVerificacionRepository codigoVerificacionRepository;

    @Autowired
    private EncuestaInicialRepository encuestaInicialRepository;

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private CicloMenstrualRepository cicloMenstrualRepository;

    @Autowired
    private MenstruacionRepository menstruacionRepository;

    @Autowired
    private AnticonceptivoRepository anticonceptivoRepository;

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private CicloMenstrualService cicloMenstrualService;

    @Autowired
    private RecordatorioService recordatorioService;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private HashContrasenia hashContrasenia;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Autowired
    private CorreoAuxiliar correoAuxiliar;

    private static final int MINUTOS_EXPIRACION_CODIGO = 15;

    private final SecureRandom generadorAleatorio = new SecureRandom();

    public int registrarUsuaria(RegistroUsuariaDTO registroUsuariaDTO) {
        return registrarUsuaria(registroUsuariaDTO, true);
    }

    public int registrarUsuaria(RegistroUsuariaDTO registroUsuariaDTO, boolean enviarCorreo) {
        if (estaVacio(registroUsuariaDTO.getPrimerNombre()) || estaVacio(registroUsuariaDTO.getPrimerApellido())
                || estaVacio(registroUsuariaDTO.getApodo()) || estaVacio(registroUsuariaDTO.getCorreoElectronico())
                || estaVacio(registroUsuariaDTO.getContrasenia()) || registroUsuariaDTO.getFechaNacimiento() == null) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (!esCorreoValido(registroUsuariaDTO.getCorreoElectronico())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (registroUsuariaDTO.getContrasenia().length() < LARGO_MINIMO_CONTRASENIA) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        if (registroUsuariaDTO.getFechaNacimiento().isAfter(LocalDate.now())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        String correo = normalizarCorreo(registroUsuariaDTO.getCorreoElectronico());
        if (usuariaRepository.existsByCorreoElectronico(correo)) {
            return CodigoRespuesta.CONFLICTO;
        }

        Usuaria usuaria = modelMapper.map(registroUsuariaDTO, Usuaria.class);
        usuaria.setCorreoElectronico(correo);
        usuaria.setContrasenia(hashContrasenia.generarHash(registroUsuariaDTO.getContrasenia()));
        usuaria.setFechaRegistro(LocalDateTime.now());
        usuaria.setMetodoCalculo(METODO_AUTOMATICO);
        usuaria.setActiva(Boolean.TRUE);
        usuaria = usuariaRepository.save(usuaria);

        if (enviarCorreo) {
            generarYEnviarCodigo(usuaria);
        } else {
            generarCodigo(usuaria);
        }
        return CodigoRespuesta.CREADO;
    }

    public int iniciarSesion(InicioSesionDTO inicioSesionDTO) {
        if (estaVacio(inicioSesionDTO.getCorreoElectronico()) || estaVacio(inicioSesionDTO.getContrasenia())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Usuaria usuaria = usuariaRepository.findByCorreoElectronico(normalizarCorreo(inicioSesionDTO.getCorreoElectronico())).orElse(null);

        if (usuaria == null || !Boolean.TRUE.equals(usuaria.getActiva())) {
            return CodigoRespuesta.NO_AUTORIZADO;
        }
        if (!hashContrasenia.verificarContrasenia(inicioSesionDTO.getContrasenia(), usuaria.getContrasenia())) {
            return CodigoRespuesta.NO_AUTORIZADO;
        }
        if (!estaVerificada(usuaria.getIdUsuario())) {
            return CodigoRespuesta.PROHIBIDO;
        }
        usuaria.setFechaUltimoAcceso(LocalDateTime.now());
        usuariaRepository.save(usuaria);
        return CodigoRespuesta.OK;
    }

    public int solicitarCodigo(SolicitudCodigoDTO solicitudCodigoDTO) {
        if (estaVacio(solicitudCodigoDTO.getCorreoElectronico())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Usuaria usuaria = usuariaRepository.findByCorreoElectronico(normalizarCorreo(solicitudCodigoDTO.getCorreoElectronico())).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (estaVerificada(usuaria.getIdUsuario())) {
            return CodigoRespuesta.CONFLICTO;
        }
        
        codigoVerificacionRepository.limpiarCodigosExpirados();
        boolean enviado = generarYEnviarCodigo(usuaria);
        if (!enviado) {
            return CodigoRespuesta.ERROR_CORREO;
        }
        return CodigoRespuesta.OK;
    }

    public int enviarCodigo(VerificacionCodigoDTO verificacionCodigoDTO) {
        if (estaVacio(verificacionCodigoDTO.getCorreoElectronico()) || estaVacio(verificacionCodigoDTO.getCodigo())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Usuaria usuaria = usuariaRepository.findByCorreoElectronico(normalizarCorreo(verificacionCodigoDTO.getCorreoElectronico())).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (estaVerificada(usuaria.getIdUsuario())) {
            return CodigoRespuesta.CONFLICTO;
        }

        Integer resultado = codigoVerificacionRepository.verificarCodigo(usuaria.getIdUsuario(), verificacionCodigoDTO.getCodigo().trim());
        if (resultado == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        switch (resultado) {
            case 1:
                return CodigoRespuesta.OK;
            case 2:
                return CodigoRespuesta.SOLICITUD_INCORRECTA;
            case 3:
                return CodigoRespuesta.CODIGO_EXPIRADO;
            default:
                return CodigoRespuesta.NO_ENCONTRADO;
        }
    }

    private boolean generarYEnviarCodigo(Usuaria usuaria) {
        String codigo = generarCodigo(usuaria);
        return correoAuxiliar.enviarCodigoVerificacion(usuaria.getCorreoElectronico(), usuaria.getApodo(),
                codigo, MINUTOS_EXPIRACION_CODIGO);
    }

    
    private String generarCodigo(Usuaria usuaria) {
        String codigo = String.format("%06d", generadorAleatorio.nextInt(1000000));
        LocalDateTime ahora = LocalDateTime.now();
        codigoVerificacionRepository.insertarCodigo(usuaria.getIdUsuario(), codigo, ahora,
                ahora.plusMinutes(MINUTOS_EXPIRACION_CODIGO));
        return codigo;
    }

    public int validarAcceso(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null || !Boolean.TRUE.equals(usuaria.getActiva())) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (!estaVerificada(idUsuario)) {
            return CodigoRespuesta.PROHIBIDO;
        }
        return CodigoRespuesta.OK;
    }

    public boolean estaVerificada(Long idUsuario) {
        return Boolean.TRUE.equals(usuariaRepository.estaVerificada(idUsuario));
    }

    private UsuariaDTO completarDatosCalculados(UsuariaDTO usuariaDTO) {
        usuariaDTO.setCuentaVerificada(estaVerificada(usuariaDTO.getIdUsuario()));
        usuariaDTO.setEncuestaCompletada(encuestaInicialRepository.existsByUsuariaIdUsuario(usuariaDTO.getIdUsuario()));
        return usuariaDTO;
    }

    public UsuariaDTO buscarUsuariaPorId(Long idUsuario) {
        Optional<Usuaria> usuaria = usuariaRepository.findById(idUsuario);
        if (!usuaria.isPresent()) {
            return null;
        }
        return completarDatosCalculados(modelMapper.map(usuaria.get(), UsuariaDTO.class));
    }

    public UsuariaDTO buscarUsuariaPorCorreo(String correoElectronico) {
        Optional<Usuaria> usuaria = usuariaRepository.findByCorreoElectronico(normalizarCorreo(correoElectronico));
        if (!usuaria.isPresent()) {
            return null;
        }
        return completarDatosCalculados(modelMapper.map(usuaria.get(), UsuariaDTO.class));
    }

    public AjustesDTO verAjustes(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return null;
        }
        AjustesDTO ajustesDTO = modelMapper.map(usuaria, AjustesDTO.class);
        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);
        if (encuesta != null) {
            modelMapper.map(encuesta, ajustesDTO);
        }
        return ajustesDTO;
    }

    public int actualizarAjustes(Long idUsuario, ActualizarAjustesDTO ajustesDTO) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (ajustesDTO.getApodo() != null && estaVacio(ajustesDTO.getApodo())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (ajustesDTO.getMetodoCalculo() != null && !METODO_AUTOMATICO.equals(ajustesDTO.getMetodoCalculo())
                && !METODO_MANUAL.equals(ajustesDTO.getMetodoCalculo())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (!EncuestaInicialService.esDatoSaludValido(ajustesDTO.getAltura(), ajustesDTO.getPeso(), ajustesDTO.getHorasSueno(), false)) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (!EncuestaInicialService.sonDuracionesValidas(ajustesDTO.getDuracionCiclo(), ajustesDTO.getDuracionMenstruacion(), false)) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);

        String metodoFinal = usuaria.getMetodoCalculo();
        if (ajustesDTO.getMetodoCalculo() != null) {
            metodoFinal = ajustesDTO.getMetodoCalculo();
        }
        if (METODO_MANUAL.equals(metodoFinal)) {
            boolean tieneDuracion = ajustesDTO.getDuracionCiclo() != null;
            if (encuesta != null && encuesta.getDuracionCiclo() != null) {
                tieneDuracion = true;
            }
            if (!tieneDuracion) {
                return CodigoRespuesta.SOLICITUD_INCORRECTA;
            }
        }

        String apodoAnterior = usuaria.getApodo();
        modelMapper.map(ajustesDTO, usuaria);
        usuariaRepository.save(usuaria);

        if (encuesta != null) {
            modelMapper.map(ajustesDTO, encuesta);
            encuestaInicialRepository.save(encuesta);
        }

        if (!usuaria.getApodo().equals(apodoAnterior)) {
            articuloService.actualizarApodoEnComentarios(idUsuario, usuaria.getApodo());
        }

        cicloMenstrualService.recalcularCiclos(idUsuario);
        return CodigoRespuesta.OK;
    }

    public int cambiarContrasenia(Long idUsuario, CambioContraseniaDTO cambioContraseniaDTO) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (estaVacio(cambioContraseniaDTO.getContraseniaActual()) || estaVacio(cambioContraseniaDTO.getContraseniaNueva())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        if (!hashContrasenia.verificarContrasenia(cambioContraseniaDTO.getContraseniaActual(), usuaria.getContrasenia())) {
            return CodigoRespuesta.NO_AUTORIZADO;
        }
        if (cambioContraseniaDTO.getContraseniaNueva().length() < LARGO_MINIMO_CONTRASENIA) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        usuaria.setContrasenia(hashContrasenia.generarHash(cambioContraseniaDTO.getContraseniaNueva()));
        usuariaRepository.save(usuaria);
        return CodigoRespuesta.OK;
    }

    public int eliminarCuenta(Long idUsuario) {
        Usuaria usuaria = usuariaRepository.findById(idUsuario).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }

        recordatorioService.eliminarRecordatoriosDeUsuaria(idUsuario);
        registroDiarioRepository.deleteAll(registroDiarioRepository.findByUsuariaIdUsuarioOrderByFechaAsc(idUsuario));
        List<Menstruacion> menstruaciones = menstruacionRepository.findByCicloMenstrualUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario);
        menstruacionRepository.deleteAll(menstruaciones);
        List<CicloMenstrual> ciclos = cicloMenstrualRepository.findByUsuariaIdUsuarioOrderByFechaInicioAsc(idUsuario);
        cicloMenstrualRepository.deleteAll(ciclos);
        List<Anticonceptivo> anticonceptivos = anticonceptivoRepository.findByUsuariaIdUsuarioOrderByFechaInicioDesc(idUsuario);
        anticonceptivoRepository.deleteAll(anticonceptivos);
        List<CodigoVerificacion> codigos = codigoVerificacionRepository.findByUsuariaIdUsuario(idUsuario);
        codigoVerificacionRepository.deleteAll(codigos);
        EncuestaInicial encuesta = encuestaInicialRepository.findByUsuariaIdUsuario(idUsuario).orElse(null);
        if (encuesta != null) {
            encuestaInicialRepository.delete(encuesta);
        }
        estadisticaService.eliminarEstadisticas(idUsuario);
        articuloService.eliminarComentariosDeUsuaria(idUsuario);
        usuariaRepository.delete(usuaria);
        return CodigoRespuesta.OK;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private boolean esCorreoValido(String correo) {
        int posicionArroba = correo.indexOf('@');
        if (posicionArroba <= 0) {
            return false;
        }
        return correo.indexOf('.', posicionArroba) > posicionArroba + 1;
    }

    private String normalizarCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        return correo.trim().toLowerCase();
    }

    public void marcarComoVerificada(Long idUsuario) {
        for (CodigoVerificacion codigo : codigoVerificacionRepository.findByUsuariaIdUsuarioAndVigenteTrue(idUsuario)) {
            codigo.setUsado(Boolean.TRUE);
            codigo.setVigente(Boolean.FALSE);
            codigoVerificacionRepository.save(codigo);
        }
    }
}
