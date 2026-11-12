package co.edu.unbosque.repository.mongo;

import co.edu.unbosque.entity.mongo.Comentario;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    List<Comentario> findByIdArticuloOrderByFechaCreacionDesc(String idArticulo);

    List<Comentario> findByAutoraIdUsuario(Long idUsuario);
}
