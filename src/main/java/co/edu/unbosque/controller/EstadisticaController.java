package co.edu.unbosque.controller;

import co.edu.unbosque.dto.EstadisticaPeriodoDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.EstadisticaService;
import co.edu.unbosque.service.UsuariaService;
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
@RequestMapping("/api/usuarias/{idUsuario}/estadisticas")
@CrossOrigin(origins = "*")
public class EstadisticaController {
    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private EstadisticaService estadisticaService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verEstadisticas(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Estadísticas de la usuaria.", estadisticaService.verEstadisticas(idUsuario));
    }

    @GetMapping("/periodo")
    public ResponseEntity<RespuestaDTO> verEstadisticasPeriodo(@PathVariable Long idUsuario, @RequestParam String desde, @RequestParam String hasta) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        EstadisticaPeriodoDTO estadistica = estadisticaService.verEstadisticasPeriodo(idUsuario, desde, hasta);
        if (estadistica == null) {
            return responder(CodigoRespuesta.SOLICITUD_INCORRECTA, "Usa el formato año-mes (2026-01) y que desde no sea después de hasta.", null);
        }
        return responder(CodigoRespuesta.OK, "Estadísticas de " + desde + " a " + hasta + ".", estadistica);
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
