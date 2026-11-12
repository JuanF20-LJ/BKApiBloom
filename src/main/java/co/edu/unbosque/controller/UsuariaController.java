package co.edu.unbosque.controller;

import co.edu.unbosque.dto.ActualizarAjustesDTO;
import co.edu.unbosque.dto.AjustesDTO;
import co.edu.unbosque.dto.CambioContraseniaDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.UsuariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarias/{idUsuario}")
@CrossOrigin(origins = "*")
public class UsuariaController {
    @Autowired
    private UsuariaService usuariaService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verUsuaria(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        return responder(CodigoRespuesta.OK, "Usuaria encontrada.", usuariaService.buscarUsuariaPorId(idUsuario));
    }

    @GetMapping("/ajustes")
    public ResponseEntity<RespuestaDTO> verAjustes(@PathVariable Long idUsuario) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        AjustesDTO ajustes = usuariaService.verAjustes(idUsuario);
        return responder(CodigoRespuesta.OK, "Ajustes de la usuaria.", ajustes);
    }

    @PutMapping("/ajustes")
    public ResponseEntity<RespuestaDTO> actualizarAjustes(@PathVariable Long idUsuario, @RequestBody ActualizarAjustesDTO ajustesDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = usuariaService.actualizarAjustes(idUsuario, ajustesDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Ajustes actualizados.", usuariaService.verAjustes(idUsuario));
        }
        return responder(codigo, "Datos inválidos. Revisa los rangos y que MANUAL tenga duración de ciclo.", null);
    }

    @PutMapping("/contrasenia")
    public ResponseEntity<RespuestaDTO> cambiarContrasenia(@PathVariable Long idUsuario, @RequestBody CambioContraseniaDTO cambioContraseniaDTO) {
        ResponseEntity<RespuestaDTO> bloqueo = validarAcceso(idUsuario);
        if (bloqueo != null) {
            return bloqueo;
        }
        int codigo = usuariaService.cambiarContrasenia(idUsuario, cambioContraseniaDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Contraseña actualizada.", null);
        }
        if (codigo == CodigoRespuesta.NO_AUTORIZADO) {
            return responder(codigo, "La contraseña actual no es correcta.", null);
        }
        return responder(codigo, "La contraseña nueva debe tener al menos 8 caracteres.", null);
    }

    @DeleteMapping
    public ResponseEntity<RespuestaDTO> eliminarCuenta(@PathVariable Long idUsuario) {
        int codigo = usuariaService.eliminarCuenta(idUsuario);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Cuenta eliminada.", null);
        }
        return responder(codigo, "La usuaria no existe.", null);
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
