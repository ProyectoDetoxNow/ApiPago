package cl.detoxnow.pago.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    private final WebClient webClient;

    public GeoService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://nominatim.openstreetmap.org")
                .build();
    }

    public Map<String, Object> obtenerCoordenadas(String direccionCompleta) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", direccionCompleta)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build()
                )
                .header("User-Agent", "DetoxNow-App")
                .retrieve()
                .bodyToMono(List.class)
                .map(list -> {
                    if (list.isEmpty()) {
                        throw new RuntimeException("Dirección no encontrada");
                    }
                    return (Map<String, Object>) list.get(0);
                })
                .block();
    }
}
