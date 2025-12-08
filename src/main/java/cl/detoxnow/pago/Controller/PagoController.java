package cl.detoxnow.pago.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cl.detoxnow.pago.Model.Pago;
import cl.detoxnow.pago.Model.Pedido;
import cl.detoxnow.pago.Service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/Api/v1/pago")
@CrossOrigin(
    origins = {
        "http://localhost:3000",
        "https://ecomerceev2-scarletjara-projects.vercel.app",
        "https://ecomerceev2-git-main-scarletjara-projects.vercel.app",
        "https://ecomerceev2-git-draft-pensive-moore-scarletjara-projects.vercel.app",
        "https://ecomerceev2-git-preview-scarletjara-projects.vercel.app",
        "https://detoxnow.vercel.app"
    },
    allowedHeaders = "*",
    methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
    }
)
@Tag(name = "Pago", description = "Endpoints para gestión de pagos y pedidos")
public class PagoController {

    @Autowired
    private PagoService service;

    // LISTAR PEDIDOS
    @Operation(summary = "Listar pedidos", description = "Devuelve todos los pedidos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/pedido")
    public List<Pedido> verPedidos() {
        return service.getAllPedidos();
    }

    // LISTAR PAGOS
    @Operation(summary = "Listar pagos", description = "Devuelve todos los pagos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/pago")
    public List<Pago> verPagos() {
        return service.getAllPagos();
    }

    // OBTENER PEDIDO POR ID
    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/pedido/{idPedido}")
    public Pedido obtenerPedido(
        @Parameter(description = "ID del pedido a consultar") 
        @PathVariable("idPedido") Long idPedido) {
        
        return service.getPedidoById(idPedido);
    }

    // OBTENER PAGO POR ID
    @Operation(summary = "Obtener pago por ID", description = "Retorna un pago según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/pago/{idPago}")
    public Pago obtenerPago(
        @Parameter(description = "ID del pago a consultar") 
        @PathVariable("idPago") Long idPago) {
        
        return service.getPagoById(idPago);
    }

    // CREAR PEDIDO DESDE CARRITO
    @Operation(
        summary = "Crear pedido",
        description = "Genera un pedido tomando datos del carrito y del usuario."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/pedido/crear/{idCarrito}/{idUsuario}")
    public Pedido crearPedido(
        @Parameter(description = "ID del carrito a procesar") 
        @PathVariable("idCarrito") int idCarrito,
        @Parameter(description = "ID del usuario que realiza el pedido") 
        @PathVariable("idUsuario") int idUsuario) {

        return service.crearPedido(idCarrito, idUsuario);
    }

    // PAGAR PEDIDO
    @Operation(
        summary = "Pagar pedido",
        description = "Registra un pago para un pedido específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago procesado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Método de pago inválido")
    })
    @PostMapping("/pedido/pagar/{idPedido}")
    public Pago pagarPedido(
        @Parameter(description = "ID del pedido a pagar") 
        @PathVariable("idPedido") Long idPedido,
        @Parameter(description = "Método de pago (ej: tarjeta, transferencia)") 
        @RequestParam String metodoPago) {

        return service.pagarPedido(idPedido, metodoPago);
    }
}
