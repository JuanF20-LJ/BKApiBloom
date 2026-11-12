package co.edu.unbosque.controller;

import co.edu.unbosque.dto.ArticuloDTO;
import co.edu.unbosque.dto.ComentarioCreacionDTO;
import co.edu.unbosque.dto.ComentarioDTO;
import co.edu.unbosque.dto.RespuestaDTO;
import co.edu.unbosque.service.ArticuloService;
import co.edu.unbosque.service.CodigoRespuesta;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {
    @Autowired
    private ArticuloService articuloService;

    @GetMapping
    public ResponseEntity<RespuestaDTO> verArticulos(@RequestParam(required = false) String categoria) {
        return responder(CodigoRespuesta.OK, "Artículos.", articuloService.verArticulos(categoria));
    }

    @GetMapping("/{idArticulo}")
    public ResponseEntity<RespuestaDTO> verArticulo(@PathVariable String idArticulo) {
        ArticuloDTO articulo = articuloService.verArticulo(idArticulo);
        if (articulo == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "El artículo no existe.", null);
        }
        return responder(CodigoRespuesta.OK, "Artículo.", articulo);
    }

    @PostMapping
    public ResponseEntity<RespuestaDTO> crearArticulo(@RequestBody ArticuloDTO articuloDTO) {
        int codigo = articuloService.crearArticulo(articuloDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            return responder(codigo, "Artículo cargado.", articuloService.verArticulo(articuloDTO.getIdArticulo()));
        }
        return responder(codigo, "Faltan título, contenido, categoría o fuente.", null);
    }

    @GetMapping("/{idArticulo}/comentarios")
    public ResponseEntity<RespuestaDTO> verComentarios(@PathVariable String idArticulo,
                                                       @RequestParam(required = false) Integer pagina,
                                                       @RequestParam(required = false) Integer tamanio) {
        List<ComentarioDTO> comentarios = articuloService.verComentarios(idArticulo, pagina, tamanio);
        if (comentarios == null) {
            return responder(CodigoRespuesta.NO_ENCONTRADO, "El artículo no existe.", null);
        }
        return responder(CodigoRespuesta.OK, "Comentarios.", comentarios);
    }

    @PostMapping("/{idArticulo}/comentarios")
    public ResponseEntity<RespuestaDTO> crearComentario(@PathVariable String idArticulo, @RequestBody ComentarioCreacionDTO comentarioDTO) {
        int codigo = articuloService.crearComentario(idArticulo, comentarioDTO);
        if (codigo == CodigoRespuesta.CREADO) {
            return responder(codigo, "Comentario publicado.", articuloService.verComentarios(idArticulo, 0, 10));
        }
        if (codigo == CodigoRespuesta.PROHIBIDO) {
            return responder(codigo, "Debes verificar tu correo para comentar.", null);
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "El artículo o la usuaria no existe.", null);
        }
        return responder(codigo, "El comentario no puede estar vacío ni pasar de 1000 caracteres.", null);
    }

    @PutMapping("/{idArticulo}/comentarios/{idComentario}")
    public ResponseEntity<RespuestaDTO> editarComentario(@PathVariable String idArticulo, @PathVariable String idComentario,
                                                         @RequestBody ComentarioCreacionDTO comentarioDTO) {
        int codigo = articuloService.editarComentario(idArticulo, idComentario, comentarioDTO);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Comentario editado.", null);
        }
        if (codigo == CodigoRespuesta.PROHIBIDO) {
            return responder(codigo, "Solo puedes editar tus propios comentarios.", null);
        }
        if (codigo == CodigoRespuesta.NO_ENCONTRADO) {
            return responder(codigo, "El comentario no existe.", null);
        }
        return responder(codigo, "El comentario no puede estar vacío ni pasar de 1000 caracteres.", null);
    }

    @DeleteMapping("/{idArticulo}/comentarios/{idComentario}")
    public ResponseEntity<RespuestaDTO> eliminarComentario(@PathVariable String idArticulo, @PathVariable String idComentario,
                                                           @RequestParam Long idUsuario) {
        int codigo = articuloService.eliminarComentario(idArticulo, idComentario, idUsuario);
        if (codigo == CodigoRespuesta.OK) {
            return responder(codigo, "Comentario eliminado.", null);
        }
        if (codigo == CodigoRespuesta.PROHIBIDO) {
            return responder(codigo, "Solo puedes eliminar tus propios comentarios.", null);
        }
        return responder(codigo, "El comentario no existe.", null);
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
