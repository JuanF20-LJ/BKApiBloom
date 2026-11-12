package co.edu.unbosque.service;

import co.edu.unbosque.dto.ActividadSexualDTO;
import co.edu.unbosque.dto.CatalogosDTO;
import co.edu.unbosque.dto.EmocionDTO;
import co.edu.unbosque.dto.SintomaDTO;
import co.edu.unbosque.entity.postgres.ActividadSexual;
import co.edu.unbosque.entity.postgres.Emocion;
import co.edu.unbosque.entity.postgres.Sintoma;
import co.edu.unbosque.repository.postgres.ActividadSexualRepository;
import co.edu.unbosque.repository.postgres.EmocionRepository;
import co.edu.unbosque.repository.postgres.SintomaRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CatalogoService {
    @Autowired
    private SintomaRepository sintomaRepository;

    @Autowired
    private EmocionRepository emocionRepository;

    @Autowired
    private ActividadSexualRepository actividadSexualRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public List<SintomaDTO> listarSintomas() {
        List<SintomaDTO> lista = new ArrayList<SintomaDTO>();
        for (Sintoma sintoma : sintomaRepository.findAllByOrderByNombreAsc()) {
            lista.add(modelMapper.map(sintoma, SintomaDTO.class));
        }
        return lista;
    }

    public List<EmocionDTO> listarEmociones() {
        List<EmocionDTO> lista = new ArrayList<EmocionDTO>();
        for (Emocion emocion : emocionRepository.findAllByOrderByNombreAsc()) {
            lista.add(modelMapper.map(emocion, EmocionDTO.class));
        }
        return lista;
    }

    public List<ActividadSexualDTO> listarActividadesSexuales() {
        List<ActividadSexualDTO> lista = new ArrayList<ActividadSexualDTO>();
        for (ActividadSexual actividad : actividadSexualRepository.findAllByOrderByNombreAsc()) {
            lista.add(modelMapper.map(actividad, ActividadSexualDTO.class));
        }
        return lista;
    }

    public CatalogosDTO verCatalogos() {
        CatalogosDTO catalogos = new CatalogosDTO();
        catalogos.setSintomas(listarSintomas());
        catalogos.setEmociones(listarEmociones());
        catalogos.setActividadesSexuales(listarActividadesSexuales());
        return catalogos;
    }
}
