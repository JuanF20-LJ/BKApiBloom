package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.Anticonceptivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnticonceptivoRepository extends JpaRepository<Anticonceptivo, Long> {
    List<Anticonceptivo> findByUsuariaIdUsuarioOrderByFechaInicioDesc(Long idUsuario);
}
