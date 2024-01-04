package dan.ms.tp.mspedidos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // @GetMapping
    // public ResponseEntity<List<Pedido>> buscar(){
    //     return ResponseEntity.ok().body(repo.findAll());
    // }
}
