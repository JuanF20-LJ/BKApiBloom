package co.edu.unbosque.controller;

import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.CatalogoService;
import co.edu.unbosque.service.CodigoRespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = "*")
public class CatalogoController {
    @Autowired
    private CatalogoService catalogoService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verCatalogos() {
        return responder(CodigoRespuesta.OK, "Catálogos.", catalogoService.verCatalogos());
    }

    @GetMapping("/sintomas")
    public ResponseEntity<RespuestaDTO> verSintomas() {
        return responder(CodigoRespuesta.OK, "Síntomas.", catalogoService.listarSintomas());
    }

    @GetMapping("/emociones")
    public ResponseEntity<RespuestaDTO> verEmociones() {
        return responder(CodigoRespuesta.OK, "Emociones.", catalogoService.listarEmociones());
    }

    @GetMapping("/actividades-sexuales")
    public ResponseEntity<RespuestaDTO> verActividadesSexuales() {
        return responder(CodigoRespuesta.OK, "Actividades sexuales.", catalogoService.listarActividadesSexuales());
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
