package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.RecordatorioMenstruacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordatorioMenstruacionRepository extends JpaRepository<RecordatorioMenstruacion, Long> {
    List<RecordatorioMenstruacion> findByCicloMenstrualUsuariaIdUsuario(Long idUsuario);
}
