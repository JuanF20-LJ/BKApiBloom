package co.edu.unbosque.controller;

import co.edu.unbosque.dto.CalendarioDTO;
import co.edu.unbosque.dto.CicloActualDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CalendarioService;
import co.edu.unbosque.service.CicloMenstrualService;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.UsuariaService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}")
@CrossOrigin(origins = "*")
public class CalendarioController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private CicloMenstrualService cicloMenstrualService;

    @GetMapping("/calendario")
    public ResponseEntity<RespuestaDTO> llenarCalendario(@PathVariable Long idUsuario,
                                                         @RequestParam(required = false) Integer anio,
                                                         @RequestParam(required = false) Integer mes) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        LocalDate hoy = LocalDate.now();
        int anioConsulta = hoy.getYear();
        int mesConsulta = hoy.getMonthValue();
        if (anio != null) {
            anioConsulta = anio;
        }
        if (mes != null) {
            mesConsulta = mes;
        }
        CalendarioDTO calendario = calendarioService.llenarCalendario(idUsuario, anioConsulta, mesConsulta);
        if (calendario == null) {
            return responder(CodigoRespuesta.SOLICITUD_INCORRECTA, "Mes (1-12) o año inválido.", null);
        }
        return responder(CodigoRespuesta.OK, "Calendario de " + mesConsulta + "/" + anioConsulta + ".", calendario);
    }

    @GetMapping("/ciclo-actual")
    public ResponseEntity<RespuestaDTO> verCicloActual(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        CicloActualDTO cicloActual = cicloMenstrualService.obtenerCicloActual(idUsuario);
        if (cicloActual == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "Aún no hay ciclos. Registra tu primera menstruación o llena la encuesta.", null);
        }
        return responder(CodigoRespuesta.OK, "Ciclo actual.", cicloActual);
    }

    @GetMapping("/ciclos")
    public ResponseEntity<RespuestaDTO> verCiclos(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Ciclos de la usuaria.", cicloMenstrualService.listarCiclos(idUsuario));
    }

    @GetMapping("/proximas-menstruaciones")
    public ResponseEntity<RespuestaDTO> verProximasMenstruaciones(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Próximas menstruaciones estimadas.", cicloMenstrualService.listarProximasMenstruaciones(idUsuario));
    }

    private HttpStatus convertirCodigo(int codigo) {
        switch (codigo) {
            case CodigoRespuesta.OK:
                return HttpStatus.OK;
            case CodigoRespuesta.CREADO:
                return HttpStatus.CREATED;
            case CodigoRespuesta.SOLICITUD_INCORRECTA:
                return HttpStatus.BAD_REQUEST;
            case CodigoRespuesta.NO_AUTORIZADO:
                return HttpStatus.UNAUTHORIZED;
            case CodigoRespuesta.NO_ENCONTRADO:
                return HttpStatus.NOT_FOUND;
            case CodigoRespuesta.CONFLICTO:
                return HttpStatus.CONFLICT;
            case CodigoRespuesta.PROHIBIDO:
                return HttpStatus.FORBIDDEN;
            case CodigoRespuesta.CODIGO_EXPIRADO:
                return HttpStatus.GONE;
            case CodigoRespuesta.SERVICIO_NO_DISPONIBLE:
                return HttpStatus.SERVICE_UNAVAILABLE;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private ResponseEntity<RespuestaDTO> responder(int codigo, String mensaje, Object datos) {
        return new ResponseEntity<RespuestaDTO>(new RespuestaDTO(codigo, mensaje, datos), convertirCodigo(codigo));
    }

    private ResponseEntity<RespuestaDTO> validarAcceso(Long idUsuario) {
        int codigo = usuariaService.validarAcceso(idUsuario);
        if (codigo == CodigoRespuesta.OK) {
            return null;
        }
        if (codigo == CodigoRespuesta.PROHIBIDO) {
            return responder(codigo, "Debes verificar tu correo antes de usar el aplicativo.", null);
        }
        return responder(codigo, "La usuaria no existe.", null);
    }
}
