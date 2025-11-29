package cl.detoxnow.pago.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CarritoDTO {
    private int id;
    private int idUsuario;
    private String estado;
    private LocalDateTime fechaCreacion;

    private List<DetalleCarritoDTO> detalles;

    // Opcional: información traída desde API de usuarios
    private UsuarioDTO usuario;
}
