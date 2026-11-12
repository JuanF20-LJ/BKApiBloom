package co.edu.unbosque.controller;

import co.edu.unbosque.dto.AnticonceptivoDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.AnticonceptivoService;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.UsuariaService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}/anticonceptivos")
@CrossOrigin(origins = "*")
public class AnticonceptivoController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private AnticonceptivoService anticonceptivoService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verAnticonceptivos(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Métodos anticonceptivos.", anticonceptivoService.listarAnticonceptivos(idUsuario));
    }

    @GetMapping("/{idAnticonceptivo}")
    public ResponseEntity<RespuestaDTO> verAnticonceptivo(@PathVariable Long idUsuario, @PathVariable Long idAnticonceptivo) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        AnticonceptivoDTO anticonceptivo = anticonceptivoService.buscarAnticonceptivo(idUsuario, idAnticonceptivo);
        if (anticonceptivo == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "El método no existe.", null);
        }
        return responder(CodigoRespuesta.OK, "Método anticonceptivo.", anticonceptivo);
    }

    @PostMapping
    public ResponseEntity<RespuestaDTO> registrarAnticonceptivo(@PathVariable Long idUsuario, @RequestBody AnticonceptivoDTO anticonceptivoDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = anticonceptivoService.registrarAnticonceptivo(idUsuario, anticonceptivoDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            return responder(codigo, "Método registrado.", anticonceptivoService.buscarAnticonceptivo(idUsuario, anticonceptivoDTO.getIdAnticonceptivo()));
        }
        return responder(codigo, "Datos inválidos: nombre, fechaInicio y proximaAplicacion obligatorios; estado ACTIVO/SUSPENDIDO/FINALIZADO; fechas coherentes.", null);
    }

    @PutMapping("/{idAnticonceptivo}")
    public ResponseEntity<RespuestaDTO> editarAnticonceptivo(@PathVariable Long idUsuario, @PathVariable Long idAnticonceptivo,
                                                             @RequestBody AnticonceptivoDTO anticonceptivoDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = anticonceptivoService.editarAnticonceptivo(idUsuario, idAnticonceptivo, anticonceptivoDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Método actualizado.", anticonceptivoService.buscarAnticonceptivo(idUsuario, idAnticonceptivo));
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "El método no existe.", null);
        }
        return responder(codigo, "Datos inválidos.", null);
    }

    @PutMapping("/{idAnticonceptivo}/aplicacion")
    public ResponseEntity<RespuestaDTO> registrarAplicacion(@PathVariable Long idUsuario, @PathVariable Long idAnticonceptivo) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = anticonceptivoService.registrarAplicacion(idUsuario, idAnticonceptivo);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Aplicación registrada.", anticonceptivoService.buscarAnticonceptivo(idUsuario, idAnticonceptivo));
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "El método no está activo.", null);
        }
        if (codigo == CodigoRespuesta.SOLICITUD_INCORRECTA) {
            return responder(codigo, "El método no tiene frecuenciaDias; edítalo y agrégala.", null);
        }
        return responder(codigo, "El método no existe.", null);
    }

    @DeleteMapping("/{idAnticonceptivo}")
    public ResponseEntity<RespuestaDTO> eliminarAnticonceptivo(@PathVariable Long idUsuario, @PathVariable Long idAnticonceptivo) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = anticonceptivoService.eliminarAnticonceptivo(idUsuario, idAnticonceptivo);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Método eliminado.", null);
        }
        return responder(codigo, "El método no existe.", null);
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
