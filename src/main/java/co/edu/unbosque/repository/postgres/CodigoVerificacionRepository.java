package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.CodigoVerificacion;
import co.edu.unbosque.entity.postgres.CodigoVerificacionId;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion, CodigoVerificacionId> {
    List<CodigoVerificacion> findByUsuariaIdUsuario(Long idUsuario);

    List<CodigoVerificacion> findByUsuariaIdUsuarioAndVigenteTrue(Long idUsuario);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO codigo_verificacion (usuaria_idusuario, codigo, fechageneracion, fechaexpiracion, usado, vigente) "
            + "VALUES (CAST(:idUsuario AS INTEGER), :codigo, :fechaGeneracion, :fechaExpiracion, FALSE, TRUE)", nativeQuery = true)
    int insertarCodigo(@Param("idUsuario") Long idUsuario, @Param("codigo") String codigo,
                       @Param("fechaGeneracion") LocalDateTime fechaGeneracion, @Param("fechaExpiracion") LocalDateTime fechaExpiracion);

    @Query(value = "SELECT pkg_usuaria.fn_verificar_codigo(CAST(:idUsuario AS INTEGER), CAST(:codigo AS VARCHAR))", nativeQuery = true)
    Integer verificarCodigo(@Param("idUsuario") Long idUsuario, @Param("codigo") String codigo);

    @Query(value = "SELECT pkg_usuaria.sp_limpiar_codigos_expirados()", nativeQuery = true)
    Integer limpiarCodigosExpirados();
}
