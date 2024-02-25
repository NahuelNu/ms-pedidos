package dan.ms.tp.mspedidos.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.modelo.HistorialEstado;
import dan.ms.tp.mspedidos.modelo.Pedido;

@Service
public class PedidoServiceImpl implements PedidoService{
    @Autowired
    PedidoRepository pedidoRepo;
    
    @Override
    public ResponseEntity<Pedido> savePedido(Pedido pedido) {
        Instant fechaActual = Instant.now();
        pedido.setFecha(fechaActual);

        pedido.getDetallePedido().forEach(p->p.setTotal(p.getProducto().getPrecio()*p.getCantidad()*(1-p.getDescuento()/100)));
        Double precioTotal = pedido.getDetallePedido().stream().mapToDouble(p->p.getTotal()).sum();
        pedido.setTotal(precioTotal);

        HistorialEstado historialEstado = new HistorialEstado();
        historialEstado.setFechaEstado(fechaActual);

        // Falta lógica de setear Estado. Validar que 
        // total pedido < max cuenta corriente de cliente (dato a solicitar de ms-usuarios)
        // stock solicitado de p < stock actual de p (dato a solicitar de ms-productos)

        pedido.setEstados(new ArrayList<>());
        pedido.getEstados().add(historialEstado);

        pedidoRepo.save(pedido);
        return ResponseEntity.ok().body(pedido);
    }

    @Override
    public ResponseEntity<Pedido> buscarPorId(String id) {
        return ResponseEntity.of(pedidoRepo.findById(id)); 
    }

    @Override
    public ResponseEntity<List<Pedido>> buscarPorIdCliente(Integer idCliente) {
        return ResponseEntity.ok(pedidoRepo.findByCliente(idCliente));
    }

    @Override
    public ResponseEntity<List<Pedido>> buscarPorIdClienteYFechas(Integer idCliente, Instant fechaInicio,
            Instant fechaFin) {
        return ResponseEntity.ok(pedidoRepo.findByClienteFecha(idCliente, fechaInicio, fechaFin)) ;
    }

    @Override
    public ResponseEntity<List<Pedido>> buscarPorFechas(Instant fechaInicio, Instant fechaFin) {
        return ResponseEntity.ok(pedidoRepo.findByFecha(fechaInicio, fechaFin));
    }
    
}
