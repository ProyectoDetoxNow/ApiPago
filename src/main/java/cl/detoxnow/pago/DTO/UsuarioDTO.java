package cl.detoxnow.pago.DTO;

import lombok.Data;
import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UsuarioDTO {

    @Schema(description = "ID del usuario")
    private Long id;

    @Schema(description = "Nombre del usuario")
    private String nombre;

    @Schema(description = "email de usuario")
    private String email;

    @Schema(description = "Telefono")
    private Integer telefono;

    @Schema(description = "direccion")
    private String direccion;

    @Schema(description = "comuna")
    private String comuna;

    @Schema(description = "region")
    private String region;

    @Schema(description = "Contraseña")
    private String password;
}





