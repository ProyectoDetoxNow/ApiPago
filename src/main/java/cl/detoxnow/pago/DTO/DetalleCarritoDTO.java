package cl.detoxnow.pago.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DetalleCarritoDTO {

    @Schema(description = "Id del detalle del carrito")
    private int id;

    @Schema(description = "ID del producto")
    private int idProducto;

    @Schema(description = "Cantidad del producto en el carrito")
    private int cantidad;

    // Opcional: información traída desde la API de inventario
    @Schema(description = "Información del producto")
    private ProductoDTO producto;
}