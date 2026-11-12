package co.edu.unbosque.repository.postgres;

import co.edu.unbosque.entity.postgres.RecordatorioAnticonceptivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordatorioAnticonceptivoRepository extends JpaRepository<RecordatorioAnticonceptivo, Long> {
    List<RecordatorioAnticonceptivo> findByAnticonceptivoUsuariaIdUsuario(Long idUsuario);

    List<RecordatorioAnticonceptivo> findByAnticonceptivoIdAnticonceptivo(Long idAnticonceptivo);
}
