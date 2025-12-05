package cl.detoxnow.pago.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cl.detoxnow.pago.Model.Pago;
import cl.detoxnow.pago.Model.Pedido;
import cl.detoxnow.pago.Service.PagoService;


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
public class PagoController {

    @Autowired
    private PagoService service;

    // LISTAR PEDIDOS
    @GetMapping("/pedido")
    public List<Pedido> verPedidos() {
        return service.getAllPedidos();
    }

    // LISTAR PAGOS
    @GetMapping("/pago")
    public List<Pago> verPagos() {
        return service.getAllPagos();
    }

    // OBTENER PEDIDO POR ID
    @GetMapping("/pedido/{idPedido}")
    public Pedido obtenerPedido(@PathVariable("idPedido") Long idPedido) {
        return service.getPedidoById(idPedido);
    }

    // OBTENER PAGO POR ID
    @GetMapping("/pago/{idPago}")
    public Pago obtenerPago(@PathVariable("idPago") Long idPago) {
        return service.getPagoById(idPago);
    }

    // CREAR PEDIDO DESDE CARRITO (RECIBE idCarrito y idUsuario)
    @PostMapping("/pedido/crear/{idCarrito}/{idUsuario}")
    public Pedido crearPedido(
            @PathVariable("idCarrito") int idCarrito,
            @PathVariable("idUsuario") int idUsuario) {

        return service.crearPedido(idCarrito, idUsuario);
    }

    // PAGAR UN PEDIDO
    @PostMapping("/pedido/pagar/{idPedido}")
    public Pago pagarPedido(
            @PathVariable("idPedido") Long idPedido,
            @RequestParam String metodoPago) {

        return service.pagarPedido(idPedido, metodoPago);
    }
}