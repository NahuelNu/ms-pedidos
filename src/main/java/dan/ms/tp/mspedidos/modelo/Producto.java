package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Producto {
    @NotNull
    private Integer id;
    private String nombre;
    @NotNull
    @Min(value=0)
    private Double precio;
}
