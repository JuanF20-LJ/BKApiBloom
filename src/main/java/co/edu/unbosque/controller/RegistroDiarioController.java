package co.edu.unbosque.controller;

import co.edu.unbosque.dto.RegistroDiarioDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.RegistroDiarioService;
import co.edu.unbosque.service.UsuariaService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}/registros-diarios")
@CrossOrigin(origins = "*")
public class RegistroDiarioController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private RegistroDiarioService registroDiarioService;

    @PostMapping
    public ResponseEntity<RespuestaDTO> registrarDia(@PathVariable Long idUsuario, @RequestBody RegistroDiarioDTO registroDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = registroDiarioService.registrarDia(idUsuario, registroDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            LocalDate fecha = registroDTO.getFecha();
            if (fecha == null) {
                fecha = LocalDate.now();
            }
            return responder(codigo, "Día registrado.", registroDiarioService.buscarDia(idUsuario, fecha));
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "Ese día ya tiene registro. Usa PUT para editarlo.", null);
        }
        return responder(codigo, "Datos inválidos: no se registran días futuros, el tipo de flujo debe ser MANCHADO/LIGERO/MEDIO/ABUNDANTE "
                + "y los ids de síntomas, emociones y actividades deben existir.", null);
    }

    @PutMapping("/{fecha}")
    public ResponseEntity<RespuestaDTO> editarDia(@PathVariable Long idUsuario,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                                  @RequestBody RegistroDiarioDTO registroDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = registroDiarioService.editarDia(idUsuario, fecha, registroDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Día actualizado.", registroDiarioService.buscarDia(idUsuario, fecha));
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "Ese día no tiene registro. Usa POST para crearlo.", null);
        }
        return responder(codigo, "Datos inválidos.", null);
    }

    @DeleteMapping("/{fecha}")
    public ResponseEntity<RespuestaDTO> eliminarDia(@PathVariable Long idUsuario,
                                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = registroDiarioService.eliminarDia(idUsuario, fecha);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Registro eliminado.", null);
        }
        return responder(codigo, "Ese día no tiene registro.", null);
    }

    @GetMapping("/{fecha}")
    public ResponseEntity<RespuestaDTO> verDia(@PathVariable Long idUsuario,
                                               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        RegistroDiarioDTO registro = registroDiarioService.buscarDia(idUsuario, fecha);
        if (registro == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "Ese día no tiene registro.", null);
        }
        return responder(CodigoRespuesta.OK, "Registro del día.", registro);
    }

    @GetMapping
    public ResponseEntity<RespuestaDTO> verDias(@PathVariable Long idUsuario,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Registros diarios.", registroDiarioService.listarDias(idUsuario, desde, hasta));
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
