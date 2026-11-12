package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.Sintoma;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {
    List<Sintoma> findAllByOrderByNombreAsc();
}
