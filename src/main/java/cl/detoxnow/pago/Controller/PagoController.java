package cl.detoxnow.pago.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cl.detoxnow.pago.Model.Pago;
import cl.detoxnow.pago.Model.Pedido;
import cl.detoxnow.pago.Service.PagoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Api/v1/pago")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping("/pedido")
    public List<Pedido> verPedidos() {
        return service.getAllPedidos();
    }

    @GetMapping("/pago")
    public List<Pago> verPagos() {
    return service.getAllPagos();
    }

    @GetMapping("/pedido/{idPedido}")
    public Pedido obtenerPedido(@PathVariable("idPedido") Long idPedido) {
        return service.getPedidoById(idPedido);
    }

// Obtener pago por ID
@GetMapping("/pago/{idPago}")
public Pago obtenerPago(@PathVariable("idPago") Long idPago) {
    return service.getPagoById(idPago);
}

    //  Crear pedido desde carrito
    @PostMapping("/pedido/crear/{idUsuario}")
    public Pedido crearPedido(
        @PathVariable("idUsuario") int idUsuario) {

    return service.crearPedido(idUsuario);
    }

    // Pagar pedido
    @PostMapping("/pedido/pagar/{idPedido}")
    public Pago pagarPedido(
            @PathVariable("idPedido") Long idPedido,
            @RequestParam String metodoPago) {

        return service.pagarPedido(idPedido, metodoPago);
    }
}
