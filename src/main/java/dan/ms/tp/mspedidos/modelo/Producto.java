package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Producto {
    @NotNull
    private Integer id;
    private String nombre;
    @NotNull
    private Double precio;
}
