package dan.ms.tp.mspedidos.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.modelo.Pedido;

@Service
public interface PedidoService {
    public ResponseEntity<Pedido>savePedido(Pedido pedido);
    
}
