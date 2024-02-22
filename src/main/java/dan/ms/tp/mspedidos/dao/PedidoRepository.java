package dan.ms.tp.mspedidos.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import dan.ms.tp.mspedidos.modelo.Pedido;
import java.time.Instant;
import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido,String> {
    
    // List<Pedido> findByClienteRazonSocial(String razonSocial);

    @Query("{'cliente.id': ?0, 'fecha': {$gte: ?1, $lte: ?2}}")
    List<Pedido> findByClienteFecha(Integer idCliente, Instant fechaInicio, Instant fechaFin);

    @Query("{'cliente.id':?0}")
    List<Pedido> findByCliente(Integer idCliente);

    @Query("{'fecha': {$gte: ?0, $lte: ?1}}")
    List<Pedido> findByFecha(Instant fechaInicio, Instant fechaFin);
    
}