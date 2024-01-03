package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoDetalle {
    @Valid
    @NotNull
    private Producto producto;
    @NotNull
    private Integer cantidad;
    private Double descuento;
    private Double total;
}
