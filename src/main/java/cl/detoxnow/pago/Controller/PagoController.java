package cl.detoxnow.pago.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cl.detoxnow.pago.Model.Pago;
import cl.detoxnow.pago.Model.Pedido;
import cl.detoxnow.pago.Service.PagoService;

@RestController
@RequestMapping("/Api/v1/pago")
public class PagoController {

    @Autowired
    private PagoService service;

    // ✅ 4️⃣ Crear pedido desde carrito
    @PostMapping("/pedido/crear/{idUsuario}")
    public Pedido crearPedido(
            @PathVariable int idUsuario,
            @RequestBody String productosJson,
            @RequestParam double total) {

        return service.crearPedido(idUsuario, productosJson, total);
    }

    // ✅ 5️⃣ Pagar pedido
    @PostMapping("/pedido/pagar/{idPedido}")
    public Pago pagarPedido(
            @PathVariable Long idPedido,
            @RequestParam String metodoPago) {

        return service.pagarPedido(idPedido, metodoPago);
    }
}
