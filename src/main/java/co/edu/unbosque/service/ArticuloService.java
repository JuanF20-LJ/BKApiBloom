package co.edu.unbosque.service;

import co.edu.unbosque.dto.ArticuloDTO;
import co.edu.unbosque.dto.ComentarioCreacionDTO;
import co.edu.unbosque.dto.ComentarioDTO;
import co.edu.unbosque.entity.mongo.Articulo;
import co.edu.unbosque.entity.mongo.AutoraComentario;
import co.edu.unbosque.entity.mongo.Comentario;
import co.edu.unbosque.entity.mongo.ComentarioReciente;
import co.edu.unbosque.entity.postgres.Usuaria;
import co.edu.unbosque.repository.mongo.ArticuloRepository;
import co.edu.unbosque.repository.mongo.ComentarioRepository;
import co.edu.unbosque.repository.postgres.UsuariaRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticuloService {
    public static final int MAXIMO_COMENTARIOS_RECIENTES = 10;

    private static final int LARGO_MAXIMO_COMENTARIO = 1000;

    private static final int TAMANIO_PAGINA_POR_DEFECTO = 10;

    private static final int VERSION_ESQUEMA = 1;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuariaRepository usuariaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<Comentario, ComentarioDTO>() {
            @Override
            protected void configure() {
                map().setIdUsuario(source.getAutora().getIdUsuario());
                map().setApodo(source.getAutora().getApodo());
            }
        });

        modelMapper.addMappings(new PropertyMap<ComentarioReciente, ComentarioDTO>() {
            @Override
            protected void configure() {
                map().setIdUsuario(source.getAutora().getIdUsuario());
                map().setApodo(source.getAutora().getApodo());
            }
        });
    }

    public List<ArticuloDTO> verArticulos(String categoria) {
        List<Articulo> articulos;
        if (categoria == null || categoria.trim().isEmpty()) {
            articulos = articuloRepository.findAllByOrderByFechaPublicacionDesc();
        } else {
            articulos = articuloRepository.findByCategoriaOrderByFechaPublicacionDesc(categoria.trim());
        }
        List<ArticuloDTO> lista = new ArrayList<ArticuloDTO>();
        for (Articulo articulo : articulos) {
            lista.add(modelMapper.map(articulo, ArticuloDTO.class));
        }
        return lista;
    }

    public ArticuloDTO verArticulo(String idArticulo) {
        Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
        if (articulo == null) {
            return null;
        }
        return modelMapper.map(articulo, ArticuloDTO.class);
    }

    public int crearArticulo(ArticuloDTO articuloDTO) {
        if (estaVacio(articuloDTO.getTitulo()) || estaVacio(articuloDTO.getContenido()) || estaVacio(articuloDTO.getCategoria())
                || articuloDTO.getFuente() == null || estaVacio(articuloDTO.getFuente().getNombre())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        Articulo articulo = modelMapper.map(articuloDTO, Articulo.class);
        articulo.setIdArticulo(null);
        articulo.setSchemaVersion(VERSION_ESQUEMA);
        articulo.setTotalComentarios(0);
        articulo.setComentariosRecientes(new ArrayList<ComentarioReciente>());
        articuloRepository.save(articulo);
        articuloDTO.setIdArticulo(articulo.getIdArticulo());
        return CodigoRespuesta.CREADO;
    }

    public List<ComentarioDTO> verComentarios(String idArticulo, Integer pagina, Integer tamanio) {
        if (!articuloRepository.existsById(idArticulo)) {
            return null;
        }
        int numeroPagina = 0;
        if (pagina != null && pagina > 0) {
            numeroPagina = pagina;
        }
        int tamanioPagina = TAMANIO_PAGINA_POR_DEFECTO;
        if (tamanio != null && tamanio > 0 && tamanio <= 50) {
            tamanioPagina = tamanio;
        }
        int desde = numeroPagina * tamanioPagina;
        int hasta = desde + tamanioPagina;

        List<ComentarioDTO> lista = new ArrayList<ComentarioDTO>();
        int posicion = 0;
        for (Comentario comentario : comentarioRepository.findByIdArticuloOrderByFechaCreacionDesc(idArticulo)) {
            if (posicion >= desde && posicion < hasta) {
                lista.add(modelMapper.map(comentario, ComentarioDTO.class));
            }
            posicion++;
        }
        return lista;
    }

    public int crearComentario(String idArticulo, ComentarioCreacionDTO comentarioDTO) {
        Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
        if (articulo == null || comentarioDTO.getIdUsuario() == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        Usuaria usuaria = usuariaRepository.findById(comentarioDTO.getIdUsuario()).orElse(null);
        if (usuaria == null) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (!Boolean.TRUE.equals(usuariaRepository.estaVerificada(usuaria.getIdUsuario()))) {
            return CodigoRespuesta.PROHIBIDO;
        }
        if (!esContenidoValido(comentarioDTO.getContenido())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }

        Comentario comentario = modelMapper.map(comentarioDTO, Comentario.class);
        comentario.setContenido(comentarioDTO.getContenido().trim());
        comentario.setSchemaVersion(VERSION_ESQUEMA);
        comentario.setIdArticulo(idArticulo);
        comentario.setAutora(crearAutora(usuaria));
        comentario.setFechaCreacion(LocalDateTime.now());
        comentario.setFechaEdicion(null);
        comentarioRepository.save(comentario);

        articulo.getComentariosRecientes().add(0, modelMapper.map(comentario, ComentarioReciente.class));
        while (articulo.getComentariosRecientes().size() > MAXIMO_COMENTARIOS_RECIENTES) {
            articulo.getComentariosRecientes().remove(articulo.getComentariosRecientes().size() - 1);
        }

        articulo.setTotalComentarios(articulo.getTotalComentarios() + 1);
        articuloRepository.save(articulo);
        return CodigoRespuesta.CREADO;
    }

    public int editarComentario(String idArticulo, String idComentario, ComentarioCreacionDTO comentarioDTO) {
        Comentario comentario = comentarioRepository.findById(idComentario).orElse(null);
        if (comentario == null || !comentario.getIdArticulo().equals(idArticulo)) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (comentarioDTO.getIdUsuario() == null || !comentario.getAutora().getIdUsuario().equals(comentarioDTO.getIdUsuario())) {
            return CodigoRespuesta.PROHIBIDO;
        }
        if (!esContenidoValido(comentarioDTO.getContenido())) {
            return CodigoRespuesta.SOLICITUD_INCORRECTA;
        }
        comentario.setContenido(comentarioDTO.getContenido().trim());
        comentario.setFechaEdicion(LocalDateTime.now());
        comentarioRepository.save(comentario);

        Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
        if (articulo != null) {
            for (ComentarioReciente copia : articulo.getComentariosRecientes()) {
                if (copia.getIdComentario().equals(idComentario)) {
                    copia.setContenido(comentario.getContenido());
                    copia.setFechaEdicion(comentario.getFechaEdicion());
                }
            }
            articuloRepository.save(articulo);
        }
        return CodigoRespuesta.OK;
    }

    public int eliminarComentario(String idArticulo, String idComentario, Long idUsuario) {
        Comentario comentario = comentarioRepository.findById(idComentario).orElse(null);
        if (comentario == null || !comentario.getIdArticulo().equals(idArticulo)) {
            return CodigoRespuesta.NO_ENCONTRADO;
        }
        if (idUsuario == null || !comentario.getAutora().getIdUsuario().equals(idUsuario)) {
            return CodigoRespuesta.PROHIBIDO;
        }
        comentarioRepository.delete(comentario);
        Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
        if (articulo != null) {
            reconstruirComentariosRecientes(articulo);
        }
        return CodigoRespuesta.OK;
    }

    public void actualizarApodoEnComentarios(Long idUsuario, String apodoNuevo) {
        List<String> articulosAfectados = new ArrayList<String>();
        for (Comentario comentario : comentarioRepository.findByAutoraIdUsuario(idUsuario)) {
            comentario.getAutora().setApodo(apodoNuevo);
            comentarioRepository.save(comentario);
            if (!articulosAfectados.contains(comentario.getIdArticulo())) {
                articulosAfectados.add(comentario.getIdArticulo());
            }
        }
        for (String idArticulo : articulosAfectados) {
            Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
            if (articulo != null) {
                for (ComentarioReciente copia : articulo.getComentariosRecientes()) {
                    if (copia.getAutora().getIdUsuario().equals(idUsuario)) {
                        copia.getAutora().setApodo(apodoNuevo);
                    }
                }
                articuloRepository.save(articulo);
            }
        }
    }

    public void eliminarComentariosDeUsuaria(Long idUsuario) {
        List<String> articulosAfectados = new ArrayList<String>();
        for (Comentario comentario : comentarioRepository.findByAutoraIdUsuario(idUsuario)) {
            if (!articulosAfectados.contains(comentario.getIdArticulo())) {
                articulosAfectados.add(comentario.getIdArticulo());
            }
            comentarioRepository.delete(comentario);
        }
        for (String idArticulo : articulosAfectados) {
            Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
            if (articulo != null) {
                reconstruirComentariosRecientes(articulo);
            }
        }
    }

    private void reconstruirComentariosRecientes(Articulo articulo) {
        List<Comentario> comentarios = comentarioRepository.findByIdArticuloOrderByFechaCreacionDesc(articulo.getIdArticulo());
        List<ComentarioReciente> recientes = new ArrayList<ComentarioReciente>();
        for (Comentario comentario : comentarios) {
            if (recientes.size() < MAXIMO_COMENTARIOS_RECIENTES) {
                recientes.add(modelMapper.map(comentario, ComentarioReciente.class));
            }
        }
        articulo.setComentariosRecientes(recientes);
        articulo.setTotalComentarios(comentarios.size());
        articuloRepository.save(articulo);
    }

    private AutoraComentario crearAutora(Usuaria usuaria) {
        AutoraComentario autora = new AutoraComentario();
        autora.setIdUsuario(usuaria.getIdUsuario());
        autora.setApodo(usuaria.getApodo());
        return autora;
    }

    private boolean esContenidoValido(String contenido) {
        return !estaVacio(contenido) && contenido.trim().length() <= LARGO_MAXIMO_COMENTARIO;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
