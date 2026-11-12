package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.Menstruacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenstruacionRepository extends JpaRepository<Menstruacion, Long> {
    List<Menstruacion> findByCicloMenstrualUsuariaIdUsuarioOrderByFechaInicioAsc(Long idUsuario);
}
