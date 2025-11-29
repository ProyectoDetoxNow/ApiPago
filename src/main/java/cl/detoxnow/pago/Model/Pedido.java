package cl.detoxnow.pago.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;

    @Column(columnDefinition = "TEXT")
    private String productos;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private String estado; // PENDIENTE, PAGADO

    private LocalDateTime fecha = LocalDateTime.now();
}