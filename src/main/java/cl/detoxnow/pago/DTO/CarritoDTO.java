package cl.detoxnow.pago.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CarritoDTO {
    private int id;
    private String estado;
    private LocalDateTime fechaCreacion;

    private List<DetalleCarritoDTO> detalles;
}