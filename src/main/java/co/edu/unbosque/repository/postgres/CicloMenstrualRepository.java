package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.CicloMenstrual;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CicloMenstrualRepository extends JpaRepository<CicloMenstrual, Long> {
    List<CicloMenstrual> findByUsuariaIdUsuarioOrderByFechaInicioAsc(Long idUsuario);

    @Query(value = "SELECT pkg_ciclo.fn_duracion_promedio_ciclo(CAST(:idUsuario AS INTEGER), CAST(:cantidad AS INTEGER))", nativeQuery = true)
    Integer duracionPromedioCiclo(@Param("idUsuario") Long idUsuario, @Param("cantidad") int cantidad);

    @Query(value = "SELECT pkg_ciclo.fn_duracion_promedio_menstruacion(CAST(:idUsuario AS INTEGER), CAST(:cantidad AS INTEGER))", nativeQuery = true)
    Integer duracionPromedioMenstruacion(@Param("idUsuario") Long idUsuario, @Param("cantidad") int cantidad);
}
