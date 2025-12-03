package cl.detoxnow.pago.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.carrito.url}")
    private String carritoUrl;

    @Value("${api.usuarios.url}")
    private String usuarioUrl;

    @Value("${api.inventario.url}")
    private String inventarioUrl;
   

    private final ObjectMapper mapper = new ObjectMapper();

    // ================================================================
    // 1️⃣ CREAR PEDIDO (RECIBE idCarrito Y idUsuario)
    // ================================================================
    public Pedido crearPedido(int idCarrito, int idUsuario) {

        // 1. Validar usuario existente
        UsuarioDTO usuario;
        try {
            usuario = rest.getForObject(
                    usuarioUrl + "/" + idUsuario,
                    UsuarioDTO.class);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // 2. Obtener carrito por idCarrito (NO POR USUARIO)
        CarritoDTO carrito = rest.getForObject(
                carritoUrl + "/" + idCarrito,
                CarritoDTO.class);

        if (carrito == null || carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
        }

        // 3. Calcular total real
        double total = 0;
        for (DetalleCarritoDTO det : carrito.getDetalles()) {

            ProductoDTO producto = rest.getForObject(
                    inventarioUrl + "/" + det.getIdProducto(),
                    ProductoDTO.class);

            if (producto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + det.getIdProducto());
            }

            total += producto.getPrecio() * det.getCantidad();
        }

        // 4. Crear snapshot de carrito
        String productosJson;
        try {
            productosJson = mapper.writeValueAsString(carrito.getDetalles());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al generar snapshot del carrito");
        }

        // 5. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setIdCarrito(idCarrito);   // 🔥 AHORA SE GUARDA EL CARRITO
        pedido.setProductos(productosJson);
        pedido.setTotal(total);
        pedido.setEstado("PENDIENTE");

        return pedidoRepository.save(pedido);
    }

    // ================================================================
    // 2️⃣ PAGAR PEDIDO
    // ================================================================
    public Pago pagarPedido(Long idPedido, String metodoPago) {

        // 1. Obtener pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        // 2. Obtener carrito por idCarrito guardado en pedido
        CarritoDTO carrito = rest.getForObject(
                carritoUrl + "/" + pedido.getIdCarrito(),
                CarritoDTO.class);

        if (carrito == null || carrito.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El carrito está vacío o no existe");
        }

        // 3. Validar stock
        for (DetalleCarritoDTO det : carrito.getDetalles()) {

            ProductoDTO producto = rest.getForObject(
                    inventarioUrl + "/" + det.getIdProducto(),
                    ProductoDTO.class);

            if (producto.getCantidad() < det.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para " + producto.getNombreProducto());
            }
        }

        // 4. Descontar stock
        for (DetalleCarritoDTO det : carrito.getDetalles()) {
            rest.put(
                    inventarioUrl + "/descontar/"
                            + det.getIdProducto() + "/" + det.getCantidad(),
                    null
            );
        }

        // 5. Cerrar carrito
        rest.put(
                carritoUrl + "/cerrar/" + carrito.getId(),
                null);

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

    // ================================================================
    // MÉTODOS GETTER
    // ================================================================
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