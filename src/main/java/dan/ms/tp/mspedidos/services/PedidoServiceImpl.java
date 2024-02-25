package dan.ms.tp.mspedidos.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.modelo.EstadoPedido;
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

        // Falta lógica de setear Estado. Validar que no 
        // total pedido > max cuenta corriente de cliente (dato a solicitar de ms-usuarios)
        // stock solicitado de p > stock actual de p (dato a solicitar de ms-productos)

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

    @Override
    public ResponseEntity<Pedido> actualizar(String id) {
        Optional<Pedido> pedidoaActualizar = pedidoRepo.findById(id);
        if (pedidoaActualizar!=null) {
            List<HistorialEstado> estadosPedido = pedidoaActualizar.get().getEstados();
            EstadoPedido estado = estadosPedido.get(estadosPedido.size()-1).getEstado();
            if (estado != EstadoPedido.RECHAZADO && estado != EstadoPedido.CANCELADO
                && estado != EstadoPedido.EN_DISTRIBUCION && estado != EstadoPedido.ENTREGADO){
                    HistorialEstado estadoNuevo = new HistorialEstado();
                    estadoNuevo.setFechaEstado(Instant.now());
                    estadoNuevo.setEstado(EstadoPedido.CANCELADO);
                    estadoNuevo.setDetalle("Cliente cancela pedido");
                    estadoNuevo.setUserEstado(pedidoaActualizar.get().getUser());
                    pedidoaActualizar.get().getEstados().add(estadoNuevo);
                    return ResponseEntity.ok(pedidoRepo.save(pedidoaActualizar.get())); 
            }
            // Que retornar cuándo el estado del pedido está mal?
            else return ResponseEntity.badRequest().build();
        }
        else return ResponseEntity.notFound().build();
    }
    
}
