package cl.detoxnow.pago.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductoDTO {

    @Schema(description = "ID del producto")
    private int id;

    @Schema(description = "Nombre del producto")
    private String nombreProducto;

    @Schema(description = "Descripción del producto")
    private String descripcion;

    @Schema(description = "Categoría del producto")
    private String categoria;

    @Schema(description = "Precio unitario")
    private double precio;

    @Schema(description = "Stock disponible")
    private int cantidad;

    @Schema(description = "URL de la imagen")
    private String imagen;
}

