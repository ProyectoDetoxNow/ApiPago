package cl.detoxnow.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.detoxnow.pago.Model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}