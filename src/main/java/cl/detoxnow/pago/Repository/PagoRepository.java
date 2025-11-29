package cl.detoxnow.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.detoxnow.pago.Model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}