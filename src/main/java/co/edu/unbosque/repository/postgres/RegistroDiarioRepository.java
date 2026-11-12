package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.RegistroDiario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {
    Optional<RegistroDiario> findByUsuariaIdUsuarioAndFecha(Long idUsuario, LocalDate fecha);

    List<RegistroDiario> findByUsuariaIdUsuarioOrderByFechaAsc(Long idUsuario);

    List<RegistroDiario> findByUsuariaIdUsuarioAndFechaBetweenOrderByFechaAsc(Long idUsuario, LocalDate desde, LocalDate hasta);
}
