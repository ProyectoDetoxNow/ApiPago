package cl.detoxnow.pago.service;

import cl.detoxnow.pago.DTO.EnvioDTO;
import cl.detoxnow.pago.DTO.EnvioResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnvioService {

    private final GeoService geoService;

    // Coordenadas fijas de la bodega (ejemplo Santiago)
    private static final double BODEGA_LAT = -33.51596;
    private static final double BODEGA_LON = -70.59828;

    public EnvioService(GeoService geoService) {
        this.geoService = geoService;
    }

    public EnvioResponseDTO calcularEnvio(EnvioDTO envio) {

        String direccionCompleta =
                envio.getDireccion() + ", " +
                envio.getComuna() + ", " +
                envio.getRegion() + ", Chile";

        Map<String, Object> geoData = geoService.obtenerCoordenadas(direccionCompleta);

        double latCliente = Double.parseDouble(geoData.get("lat").toString());
        double lonCliente = Double.parseDouble(geoData.get("lon").toString());

        double distanciaRaw = calcularDistanciaKm(
                BODEGA_LAT, BODEGA_LON,
                latCliente, lonCliente
        );

        double distancia = redondear(distanciaRaw, 2);

        double costoRaw = calcularCostoEnvio(distancia);
        double costo = Math.round(costoRaw);

        int tiempo = calcularTiempoEnvio(distancia);

        EnvioResponseDTO response = new EnvioResponseDTO();
        response.setDistanciaKm(distancia);
        response.setCostoEnvio(costo);
        response.setTiempoEstimadoHoras(tiempo);

        return response;
    }

    //  Fórmula de Haversine
    private double calcularDistanciaKm(
            double lat1, double lon1,
            double lat2, double lon2) {

        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private double calcularCostoEnvio(double distanciaKm) {
        double base = 3000;
        double porKm = 150;
        return base + (distanciaKm * porKm);
    }

    private int calcularTiempoEnvio(double distanciaKm) {
        if (distanciaKm <= 10) return 24;
        if (distanciaKm <= 30) return 48;
        return 72;
    }

    private double redondear(double valor, int decimales) {
    double factor = Math.pow(10, decimales);
    return Math.round(valor * factor) / factor;
}
}
