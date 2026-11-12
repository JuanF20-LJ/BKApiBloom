package co.edu.unbosque.repository.mongo;

import co.edu.unbosque.entity.mongo.EstadisticaMensual;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadisticaMensualRepository extends MongoRepository<EstadisticaMensual, String> {
    List<EstadisticaMensual> findByIdUsuarioOrderByAnioAscMesAsc(Long idUsuario);

    void deleteByIdUsuario(Long idUsuario);
}
