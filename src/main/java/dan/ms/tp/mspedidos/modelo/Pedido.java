package dan.ms.tp.mspedidos.modelo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "dan-pedidos")
public class Pedido {

    @Id
    private String id;
    private Instant fecha;
    @NotNull
    private Integer numeroPedido;
    private String user;
    private String observaciones;
    @Valid
    @NotNull
    private Cliente cliente;
    @Valid
    @NotEmpty
    private List<PedidoDetalle> detallePedido;
    private List<HistorialEstado> estados;
    private Double total;
}
