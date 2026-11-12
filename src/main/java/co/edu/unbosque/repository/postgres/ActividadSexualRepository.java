package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.ActividadSexual;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadSexualRepository extends JpaRepository<ActividadSexual, Long> {
    List<ActividadSexual> findAllByOrderByNombreAsc();
}
