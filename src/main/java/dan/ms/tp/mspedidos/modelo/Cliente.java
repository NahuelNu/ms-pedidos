package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Cliente {
    @NotNull
    private Integer id;
    private String razonSocial;
    private String cuit;
    private Integer deuda;
    private String correoElectronico;
    private Double maximoCuentaCorriente;
}
