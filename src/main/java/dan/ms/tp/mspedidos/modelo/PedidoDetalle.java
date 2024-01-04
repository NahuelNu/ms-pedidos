package dan.ms.tp.mspedidos.modelo;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoDetalle {
    @Valid
    @NotNull
    private Producto producto;
    @NotNull
    @Range(min = 1,max = 1000)
    private Integer cantidad;
    private Double descuento;
    private Double total;
}
