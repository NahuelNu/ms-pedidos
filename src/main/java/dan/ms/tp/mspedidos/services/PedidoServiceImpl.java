package dan.ms.tp.mspedidos.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.modelo.EstadoPedido;
import dan.ms.tp.mspedidos.modelo.HistorialEstado;
import dan.ms.tp.mspedidos.modelo.Pedido;
import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

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

        // Falta lógica de setear Estado. Validar que
        // total pedido < max cuenta corriente de cliente (dato a solicitar de ms-usuarios)
        // stock solicitado de p < stock actual de p (dato a solicitar de ms-productos)

        //WebClient cta cte

        WebClient webClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(
            HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)
        )).build();

        final String urlCliente = "http://dan-gateway:8080/usuarios/api/cliente/ctaCte/"
        + pedido.getCliente().getId().toString();

        Double maxCtaCte = webClient.get()
        .uri(urlCliente).retrieve().bodyToMono(Double.class).block();
        System.out.println("MAX cta cte: " + maxCtaCte.toString());
        
        //Instanciar estado
        HistorialEstado historialEstado = new HistorialEstado();
        historialEstado.setFechaEstado(fechaActual);

        pedido.setEstados(new ArrayList<>());

        // Chequear que haya stock suficiente de cada producto

        pedido.getDetallePedido().forEach((detalle) -> {
            final String urlProductos = "http://dan-gateway:8080/productos/api/producto/"
            + detalle.getProducto().getId();
            Integer stockProd = webClient.get().uri(urlProductos).retrieve()
            .bodyToFlux(Integer.class).blockLast();

            Integer cantidadPedida = detalle.getCantidad();


            if(stockProd < cantidadPedida){
                if(historialEstado.getEstado() == null)
                    historialEstado.setEstado(EstadoPedido.SIN_STOCK);
                historialEstado.getDetalle().concat("Producto id: " + detalle.getProducto().getId().toString() +
                ", Stock: " + stockProd.toString() + ", Cantidad Pedida: " + cantidadPedida.toString());
            }


            
        });
        
        if(historialEstado.getEstado() == null){
            
            if(pedido.getTotal() < maxCtaCte)
                historialEstado.setEstado(EstadoPedido.RECHAZADO);
            else
                historialEstado.setEstado(EstadoPedido.RECIBIDO);

        }
        else{
            if(pedido.getTotal() < maxCtaCte)
                historialEstado.setEstado(EstadoPedido.RECHAZADO);
        } 

        pedido.getEstados().add(historialEstado);
        pedidoRepo.save(pedido);     
        return ResponseEntity.ok(pedido);
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
        if (pedidoaActualizar.isPresent()) {
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
