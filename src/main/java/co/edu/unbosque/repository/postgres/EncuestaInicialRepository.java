package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.EncuestaInicial;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestaInicialRepository extends JpaRepository<EncuestaInicial, Long> {
    Optional<EncuestaInicial> findByUsuariaIdUsuario(Long idUsuario);

    boolean existsByUsuariaIdUsuario(Long idUsuario);
}
