package co.edu.unbosque.controller;

import co.edu.unbosque.dto.EncuestaInicialDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.EncuestaInicialService;
import co.edu.unbosque.service.UsuariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}/encuesta")
@CrossOrigin(origins = "*")
public class EncuestaInicialController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private EncuestaInicialService encuestaInicialService;

    @PostMapping
    public ResponseEntity<RespuestaDTO> llenarEncuesta(@PathVariable Long idUsuario, @RequestBody EncuestaInicialDTO encuestaDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = encuestaInicialService.llenarEncuesta(idUsuario, encuestaDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            return responder(codigo, "Encuesta guardada. Ya puedes ver tu calendario.", encuestaInicialService.buscarEncuesta(idUsuario));
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "La encuesta ya fue diligenciada. Puedes cambiar los datos en Ajustes.", null);
        }
        return responder(codigo, "Datos inválidos: altura en metros (0.50-2.50), peso (20-300), horas de sueño (0-24) y 1 a 3 ciclos sin cruzarse "
                + "o duraciones (ciclo 15-60, menstruación 1-15).", null);
    }

    @GetMapping
    public ResponseEntity<RespuestaDTO> verEncuesta(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        EncuestaInicialDTO encuesta = encuestaInicialService.buscarEncuesta(idUsuario);
        if (encuesta == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "La usuaria aún no ha llenado la encuesta.", null);
        }
        return responder(CodigoRespuesta.OK, "Encuesta de la usuaria.", encuesta);
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
