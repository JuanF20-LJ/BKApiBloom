package co.edu.unbosque.controller;

import co.edu.unbosque.dto.InicioSesionDTO;
import co.edu.unbosque.dto.RegistroUsuariaDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.dto.SolicitudCodigoDTO;
import co.edu.unbosque.dto.UsuariaDTO;
import co.edu.unbosque.dto.VerificacionCodigoDTO;
import co.edu.unbosque.service.CodigoRespuesta;
import co.edu.unbosque.service.UsuariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacion")
@CrossOrigin(origins = "*")
public class AutenticacionController {
    @Autowired
    private UsuariaService usuariaService;

    @PostMapping("/registrar")
    public ResponseEntity<RespuestaDTO> registrar(@RequestBody RegistroUsuariaDTO registroUsuariaDTO) {
        int codigo = usuariaService.registrarUsuaria(registroUsuariaDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            UsuariaDTO usuaria = usuariaService.buscarUsuariaPorCorreo(registroUsuariaDTO.getCorreoElectronico());
            return responder(codigo, "Cuenta creada. Revisa tu correo para verificarla.", usuaria);
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "Ya existe una cuenta con ese correo.", null);
        }
        return responder(codigo, "Datos incompletos o inválidos (nombre, apellido, apodo, correo válido, contraseña de 8+ caracteres y fecha de nacimiento).", null);
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<RespuestaDTO> iniciarSesion(@RequestBody InicioSesionDTO inicioSesionDTO) {
        int codigo = usuariaService.iniciarSesion(inicioSesionDTO);
        if (codigo == CodigoRespuesta.OK) {
            UsuariaDTO usuaria = usuariaService.buscarUsuariaPorCorreo(inicioSesionDTO.getCorreoElectronico());
            return responder(codigo, "Bienvenida, " + usuaria.getApodo() + ".", usuaria);
        }
        if (codigo == CodigoRespuesta.PROHIBIDO) {
            return responder(codigo, "Tu correo aún no está verificado. Solicita un código.", null);
        }
        if (codigo == CodigoRespuesta.NO_AUTORIZADO) {
            return responder(codigo, "Correo o contraseña incorrectos.", null);
        }
        return responder(codigo, "Escribe tu correo y tu contraseña.", null);
    }

    @PostMapping("/solicitar-codigo")
    public ResponseEntity<RespuestaDTO> solicitarCodigo(@RequestBody SolicitudCodigoDTO solicitudCodigoDTO) {
        int codigo = usuariaService.solicitarCodigo(solicitudCodigoDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Te enviamos un código nuevo a tu correo.", null);
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "No existe una cuenta con ese correo.", null);
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "Esta cuenta ya está verificada.", null);
        }
        if (codigo == CodigoRespuesta.ERROR_CORREO) {
            return responder(codigo, "No se pudo enviar el correo. Intenta más tarde.", null);
        }
        return responder(codigo, "Escribe tu correo.", null);
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<RespuestaDTO> enviarCodigo(@RequestBody VerificacionCodigoDTO verificacionCodigoDTO) {
        int codigo = usuariaService.enviarCodigo(verificacionCodigoDTO);
        if (codigo == CodigoRespuesta.OK) {
            UsuariaDTO usuaria = usuariaService.buscarUsuariaPorCorreo(verificacionCodigoDTO.getCorreoElectronico());
            return responder(codigo, "Correo verificado. Ya puedes iniciar sesión.", usuaria);
        }
        if (codigo == CodigoRespuesta.CODIGO_EXPIRADO) {
            return responder(codigo, "El código venció. Solicita uno nuevo.", null);
        }
        if (codigo == CodigoRespuesta.CONFLICTO) {
            return responder(codigo, "Esta cuenta ya está verificada.", null);
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "No hay un código vigente para ese correo. Solicita uno.", null);
        }
        return responder(codigo, "El código es incorrecto.", null);
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
}
