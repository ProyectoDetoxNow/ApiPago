package cl.detoxnow.pago.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.detoxnow.pago.DTO.CarritoDTO;
import cl.detoxnow.pago.DTO.DetalleCarritoDTO;
import cl.detoxnow.pago.DTO.ProductoDTO;
import cl.detoxnow.pago.DTO.UsuarioDTO;
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

    @Autowired
    private RestTemplate rest;

    private final ObjectMapper mapper = new ObjectMapper();

    // ================================================
    // 1️⃣ CREAR PEDIDO DESDE CARRITO (TOTAL AUTOMÁTICO)
    // ================================================
    public Pedido crearPedido(int idUsuario) {

        // 1. Validar que el usuario exista
        UsuarioDTO usuario;
        try {
            usuario = rest.getForObject(
                "http://localhost:8082/api/v1/usuarios/" + idUsuario,
                UsuarioDTO.class
            );
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // 2. Obtener carrito
        CarritoDTO carrito = rest.getForObject(
                "http://localhost:8084/Api/v1/Carrito/" + idUsuario,
                CarritoDTO.class
        );

        if (carrito == null || carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
        }

        // 3. Calcular total real
        double total = 0;
        for (DetalleCarritoDTO det : carrito.getDetalles()) {
            ProductoDTO producto = rest.getForObject(
                    "http://localhost:8083/Api/v1/inventario/" + det.getIdProducto(),
                    ProductoDTO.class
            );

            if (producto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + det.getIdProducto());
            }

            total += producto.getPrecio() * det.getCantidad();
        }

        // 4. Crear snapshot del carrito (JSON)
        String productosJson;
        try {
            productosJson = mapper.writeValueAsString(carrito.getDetalles());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al serializar carrito a JSON");
        }

        // 5. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setProductos(productosJson);
        pedido.setTotal(total);
        pedido.setEstado("PENDIENTE");

        return pedidoRepository.save(pedido);
    }

    // ================================================
    // 2️⃣ PAGAR PEDIDO
    // ================================================
    public Pago pagarPedido(Long idPedido, String metodoPago) {

        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado"
                ));

        // 2. Obtener carrito del usuario de ese pedido
        CarritoDTO carrito = rest.getForObject(
                "http://localhost:8084/Api/v1/Carrito/" + pedido.getIdUsuario(),
                CarritoDTO.class
        );

        if (carrito == null || carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El carrito está vacío o no existe");
        }

        // 3. Validar stock antes de descontar
        for (var det : carrito.getDetalles()) {

            ProductoDTO producto = rest.getForObject(
                    "http://localhost:8083/Api/v1/inventario/" + det.getIdProducto(),
                    ProductoDTO.class
            );

            if (producto.getCantidad() < det.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para " + producto.getNombreProducto()
                );
            }
        }

        // 4. Descontar stock
        for (var det : carrito.getDetalles()) {
            rest.put(
                "http://localhost:8083/Api/v1/inventario/descontar/"
                + det.getIdProducto() + "/" + det.getCantidad(),
                null
            );
        }

        // 5. Cerrar carrito
        rest.put(
                "http://localhost:8084/Api/v1/Carrito/cerrar/" + carrito.getId(),
                null
        );

        // 6. Actualizar pedido
        pedido.setEstado("PAGADO");
        pedidoRepository.save(pedido);

        // 7. Registrar pago
        Pago pago = new Pago();
        pago.setPedidoId(idPedido);
        pago.setMetodoPago(metodoPago);
        pago.setEstadoPago("APROBADO");

        return pagoRepository.save(pago);
    }

    // ================================================
    // GETTERS
    // ================================================

    public Pedido getPedidoById(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado: " + idPedido));
    }

    public Pago getPagoById(Long idPago) {
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pago no encontrado: " + idPago));
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pago> getAllPagos() {
        return pagoRepository.findAll();
    }
}