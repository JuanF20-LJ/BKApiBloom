package co.edu.unbosque.seguridad;

import co.edu.unbosque.dto.AnticonceptivoDTO;
import co.edu.unbosque.dto.ArticuloDTO;
import co.edu.unbosque.dto.CicloEncuestaDTO;
import co.edu.unbosque.dto.ComentarioCreacionDTO;
import co.edu.unbosque.dto.EmocionDTO;
import co.edu.unbosque.dto.EncuestaInicialDTO;
import co.edu.unbosque.dto.FuenteDTO;
import co.edu.unbosque.dto.RegistroDiarioDTO;
import co.edu.unbosque.dto.RegistroUsuariaDTO;
import co.edu.unbosque.dto.SintomaDTO;
import co.edu.unbosque.dto.UsuariaDTO;
import co.edu.unbosque.service.AnticonceptivoService;
import co.edu.unbosque.service.ArticuloService;
import co.edu.unbosque.service.EncuestaInicialService;
import co.edu.unbosque.service.RegistroDiarioService;
import co.edu.unbosque.service.UsuariaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales {
    private static final String CORREO_DEMO = "demo@bloom.com";

    @Autowired
    private UsuariaService usuariaService;

    @Autowired
    private EncuestaInicialService encuestaInicialService;

    @Autowired
    private RegistroDiarioService registroDiarioService;

    @Autowired
    private AnticonceptivoService anticonceptivoService;

    @Autowired
    private ArticuloService articuloService;

    public void cargarDatosPrueba() {
        Long idUsuario = cargarUsuariaDemo();
        if (articuloService.verArticulos(null).isEmpty()) {
            List<String> idsArticulos = cargarArticulos();
            ComentarioCreacionDTO comentario = new ComentarioCreacionDTO();
            comentario.setIdUsuario(idUsuario);
            comentario.setContenido("¡Muy útil este artículo!");
            articuloService.crearComentario(idsArticulos.get(0), comentario);
        }
        System.out.println(">>> Usuaria demo lista: " + CORREO_DEMO + " / Bloom1234 (idUsuario = " + idUsuario + ")");
    }

    private Long cargarUsuariaDemo() {
        UsuariaDTO existente = usuariaService.buscarUsuariaPorCorreo(CORREO_DEMO);
        if (existente != null) {
            return existente.getIdUsuario();
        }
        RegistroUsuariaDTO registro = new RegistroUsuariaDTO();
        registro.setPrimerNombre("Demo");
        registro.setPrimerApellido("Bloom");
        registro.setApodo("Demo");
        registro.setFechaNacimiento(LocalDate.of(2003, 4, 15));
        registro.setCorreoElectronico(CORREO_DEMO);
        registro.setContrasenia("Bloom1234");
        usuariaService.registrarUsuaria(registro, false);
        Long idUsuario = usuariaService.buscarUsuariaPorCorreo(CORREO_DEMO).getIdUsuario();
        usuariaService.marcarComoVerificada(idUsuario);

        LocalDate hoy = LocalDate.now();
        EncuestaInicialDTO encuesta = new EncuestaInicialDTO();
        encuesta.setAltura(1.62);
        encuesta.setPeso(56.0);
        encuesta.setHorasSueno(7.0);
        encuesta.setIngresoRango(Boolean.FALSE);
        encuesta.getCiclosIniciales().add(crearCicloEncuesta(hoy.minusDays(92), 5));
        encuesta.getCiclosIniciales().add(crearCicloEncuesta(hoy.minusDays(63), 4));
        encuesta.getCiclosIniciales().add(crearCicloEncuesta(hoy.minusDays(35), 5));
        encuestaInicialService.llenarEncuesta(idUsuario, encuesta);

        for (int i = 6; i >= 3; i--) {
            RegistroDiarioDTO dia = new RegistroDiarioDTO();
            dia.setFecha(hoy.minusDays(i));
            dia.setHayMenstruacion(Boolean.TRUE);
            if (i == 6) {
                dia.setTipoFlujo("ABUNDANTE");
                dia.getSintomas().add(crearSintoma(1L));
                dia.getSintomas().add(crearSintoma(9L));
                dia.getEmociones().add(crearEmocion(8L));
                dia.setNota("Primer día, muchos cólicos.");
            } else {
                dia.setTipoFlujo("MEDIO");
                dia.getSintomas().add(crearSintoma(1L));
            }
            registroDiarioService.registrarDia(idUsuario, dia);
        }
        RegistroDiarioDTO diaSinRegla = new RegistroDiarioDTO();
        diaSinRegla.setFecha(hoy.minusDays(1));
        diaSinRegla.setHayMenstruacion(Boolean.FALSE);
        diaSinRegla.getEmociones().add(crearEmocion(1L));
        diaSinRegla.setNota("Buen día.");
        registroDiarioService.registrarDia(idUsuario, diaSinRegla);

        AnticonceptivoDTO anticonceptivo = new AnticonceptivoDTO();
        anticonceptivo.setNombre("Inyección mensual");
        anticonceptivo.setEstado("ACTIVO");
        anticonceptivo.setFechaInicio(hoy.minusDays(60));
        anticonceptivo.setProximaAplicacion(hoy.plusDays(3));
        anticonceptivo.setFrecuenciaDias(30);
        anticonceptivoService.registrarAnticonceptivo(idUsuario, anticonceptivo);
        return idUsuario;
    }

    private List<String> cargarArticulos() {
        String[][] datos = {
            {"¿Qué es el ciclo menstrual? (ejemplo)", "Ciclo menstrual"},
            {"Autocuidado durante la menstruación (ejemplo)", "Bienestar"},
            {"Síntomas comunes del ciclo (ejemplo)", "Síntomas"}
        };
        List<String> ids = new ArrayList<String>();
        int dias = 30;
        for (String[] dato : datos) {
            ArticuloDTO articulo = new ArticuloDTO();
            articulo.setTitulo(dato[0]);
            articulo.setResumen("Artículo de prueba para la categoría " + dato[1] + ".");
            articulo.setContenido("Texto de ejemplo. Reemplazar por el contenido del artículo tomado de una fuente confiable (L.1).");
            articulo.setAutor("Autor de ejemplo");
            articulo.setCategoria(dato[1]);
            articulo.setFechaPublicacion(LocalDate.now().minusDays(dias));
            FuenteDTO fuente = new FuenteDTO();
            fuente.setNombre("Fuente de ejemplo");
            fuente.setUrl("https://example.com/articulo");
            articulo.setFuente(fuente);
            articuloService.crearArticulo(articulo);
            ids.add(articulo.getIdArticulo());
            dias = dias - 10;
        }
        return ids;
    }

    private CicloEncuestaDTO crearCicloEncuesta(LocalDate inicio, int dias) {
        CicloEncuestaDTO ciclo = new CicloEncuestaDTO();
        ciclo.setFechaInicioMenstruacion(inicio);
        ciclo.setFechaFinMenstruacion(inicio.plusDays(dias - 1));
        return ciclo;
    }

    private SintomaDTO crearSintoma(Long idSintoma) {
        SintomaDTO sintoma = new SintomaDTO();
        sintoma.setIdSintoma(idSintoma);
        return sintoma;
    }

    private EmocionDTO crearEmocion(Long idEmocion) {
        EmocionDTO emocion = new EmocionDTO();
        emocion.setIdEmocion(idEmocion);
        return emocion;
    }
}
