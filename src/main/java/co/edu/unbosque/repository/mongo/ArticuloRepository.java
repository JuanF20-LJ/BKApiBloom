package co.edu.unbosque.repository.mongo;

import co.edu.unbosque.entity.mongo.Articulo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends MongoRepository<Articulo, String> {
    List<Articulo> findAllByOrderByFechaPublicacionDesc();

    List<Articulo> findByCategoriaOrderByFechaPublicacionDesc(String categoria);
}
