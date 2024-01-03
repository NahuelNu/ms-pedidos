package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Cliente {
    @NotNull
    private Integer id;
    @NotNull
    private String razonSocial;
    @NotNull
    private String cuit;
    @NotNull
    private Integer deuda;
    @NotNull
    private String correoElectronico;
    @NotNull
    private Double maximoCuentaCorriente;
}
