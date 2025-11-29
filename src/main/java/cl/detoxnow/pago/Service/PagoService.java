package cl.detoxnow.pago.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.detoxnow.pago.Model.Pago;
import cl.detoxnow.pago.Model.Pedido;
import cl.detoxnow.pago.Repository.PagoRepository;
import cl.detoxnow.pago.Repository.PedidoRepository;

@Service
public class PagoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    // ✅ 1️⃣ Crear pedido desde carrito
    public Pedido crearPedido(int idUsuario, String productosJson, double total) {

        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setProductos(productosJson);
        pedido.setTotal(total);
        pedido.setEstado("PENDIENTE");

        return pedidoRepository.save(pedido);
    }

    // ✅ 2️⃣ Procesar pago
    public Pago pagarPedido(Long idPedido, String metodoPago) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow();

        pedido.setEstado("PAGADO");
        pedidoRepository.save(pedido);

        Pago pago = new Pago();
        pago.setPedidoId(idPedido);
        pago.setMetodoPago(metodoPago);
        pago.setEstadoPago("APROBADO");

        return pagoRepository.save(pago);
    }
}