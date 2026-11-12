package co.edu.unbosque.controller;

import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.RecordatorioService;
import co.edu.unbosque.service.UsuariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}/recordatorios")
@CrossOrigin(origins = "*")
public class RecordatorioController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private RecordatorioService recordatorioService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verRecordatorios(@PathVariable Long idUsuario,
                                                         @RequestParam(required = false, defaultValue = "false") boolean soloPendientes) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Recordatorios.", recordatorioService.listarRecordatorios(idUsuario, soloPendientes));
    }

    @PutMapping("/{idRecordatorio}/leido")
    public ResponseEntity<RespuestaDTO> marcarComoLeido(@PathVariable Long idUsuario, @PathVariable Long idRecordatorio) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = recordatorioService.marcarComoLeido(idUsuario, idRecordatorio);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Recordatorio marcado como leído.", null);
        }
        return responder(codigo, "El recordatorio no existe.", null);
    }

    @DeleteMapping("/{idRecordatorio}")
    public ResponseEntity<RespuestaDTO> eliminarRecordatorio(@PathVariable Long idUsuario, @PathVariable Long idRecordatorio) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = recordatorioService.eliminarRecordatorio(idUsuario, idRecordatorio);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Recordatorio eliminado.", null);
        }
        return responder(codigo, "El recordatorio no existe.", null);
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
