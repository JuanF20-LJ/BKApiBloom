package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.Usuaria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariaRepository extends JpaRepository<Usuaria, Long> {
    Optional<Usuaria> findByCorreoElectronico(String correoElectronico);

    boolean existsByCorreoElectronico(String correoElectronico);

    @Query(value = "SELECT pkg_usuaria.fn_esta_verificada(CAST(:idUsuario AS INTEGER))", nativeQuery = true)
    Boolean estaVerificada(@Param("idUsuario") Long idUsuario);
}
