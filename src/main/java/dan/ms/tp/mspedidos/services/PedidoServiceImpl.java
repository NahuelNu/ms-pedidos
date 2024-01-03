package dan.ms.tp.mspedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.modelo.Pedido;

public class PedidoServiceImpl implements PedidoService{
    @Autowired
    PedidoRepository pedidoRepo;
    
    @Override
    public ResponseEntity<Pedido> savePedido(Pedido pedido) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'savePedido'");
    }
    
}
