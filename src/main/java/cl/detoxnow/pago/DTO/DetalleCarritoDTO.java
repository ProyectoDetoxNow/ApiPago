package cl.detoxnow.pago.DTO;

import lombok.Data;

@Data
public class DetalleCarritoDTO {
    private int id;
    private int idProducto;
    private int cantidad;

    // Opcional: información traída desde la API de inventario
    private ProductoDTO producto;
}