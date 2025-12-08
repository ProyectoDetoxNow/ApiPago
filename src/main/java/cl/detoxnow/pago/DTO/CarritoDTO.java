package cl.detoxnow.pago.DTO;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CarritoDTO {

    @Schema(description = "ID del carrito")
    private int id;

     @Schema(description = "Estado del carrito")
    private String estado;

    @Schema(description = "Fecha de creación del carrito")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Detalles del carrito")
    private List<DetalleCarritoDTO> detalles;
}