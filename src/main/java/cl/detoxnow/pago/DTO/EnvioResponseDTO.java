package cl.detoxnow.pago.DTO;

import lombok.Data;

@Data
public class EnvioResponseDTO {

    private double distanciaKm;
    private double costoEnvio;
    private int tiempoEstimadoHoras;
}
