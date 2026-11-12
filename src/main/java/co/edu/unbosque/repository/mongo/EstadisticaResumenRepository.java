package co.edu.unbosque.repository.mongo;

import co.edu.unbosque.entity.mongo.EstadisticaResumen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadisticaResumenRepository extends MongoRepository<EstadisticaResumen, Long> {
}
