package dan.ms.tp.mspedidos.services;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.modelo.Pedido;

@Service
public interface PedidoService {
    public ResponseEntity<Pedido>savePedido(Pedido pedido);
    public ResponseEntity<Pedido>buscarPorId(String id);

    public ResponseEntity<List<Pedido>>buscarPorIdCliente(Integer idCliente);
    public ResponseEntity<List<Pedido>>buscarPorIdClienteYFechas(Integer idCliente, Instant fechaInicio, Instant fechaFin);
    public ResponseEntity<List<Pedido>>buscarPorFechas(Instant fechaInicio, Instant fechaFin);
    public ResponseEntity<Pedido> actualizar(String id);
    
}
