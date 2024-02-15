package dan.ms.tp.mspedidos.controller;



import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.services.PedidoService;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {
    
    @Autowired PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody @Validated Pedido pedido){
        return pedidoService.savePedido(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable String id){
        return pedidoService.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> buscarPorClienteYOFecha(
        @RequestParam(required = false) Integer idCliente, 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaFin) {
        
        if(idCliente != null){
            if(fechaInicio == null | fechaFin == null) return pedidoService.buscarPorIdCliente(idCliente);
            else  return pedidoService.buscarPorIdClienteYFechas(idCliente, fechaInicio, fechaFin);
        }
        else if (fechaInicio != null & fechaFin != null) return pedidoService.buscarPorFechas(fechaInicio,fechaFin);
        else return ResponseEntity.badRequest().build();
    }
}
