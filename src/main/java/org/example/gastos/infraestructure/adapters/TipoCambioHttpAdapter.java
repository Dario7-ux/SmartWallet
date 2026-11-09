package org.example.gastos.infraestructure.adapters;

import org.example.gastos.application.ports.out.TipoCambioPort;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class TipoCambioHttpAdapter implements TipoCambioPort {

    private static final Logger log = LoggerFactory.getLogger(TipoCambioHttpAdapter.class);

    private final RestClient restClient;
    private final String apiUrl;

    public TipoCambioHttpAdapter(@Value("${smartwallet.tipo-cambio.api-url:https://open.er-api.com/v6/latest}") String apiUrl) {
        this.apiUrl = apiUrl;
        this.restClient = RestClient.builder().build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BigDecimal obtenerTasaDeCambio(String monedaOrigen, String monedaDestino) {
        if (monedaOrigen == null || monedaDestino == null) {
            return BigDecimal.ONE;
        }

        String origen = monedaOrigen.trim().toUpperCase();
        String destino = monedaDestino.trim().toUpperCase();

        if (origen.equals(destino)) {
            return BigDecimal.ONE;
        }

        try {
            // Consulta en tiempo real a la API externa de tipos de cambio sin datos quemados
            Map<String, Object> response = restClient.get()
                    .uri(apiUrl + "/" + origen)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                if (rates != null && rates.containsKey(destino)) {
                    Object rateVal = rates.get(destino);
                    BigDecimal tasa = new BigDecimal(rateVal.toString()).setScale(6, RoundingMode.HALF_UP);
                    log.info("[HTTP API EXTERNA] Tasa obtenida en vivo de {} a {}: {}", origen, destino, tasa);
                    return tasa;
                }
            }
            throw new DatoInvalidoException("La API externa no retornó tasa de cambio para " + destino);
        } catch (Exception ex) {
            log.warn("[HTTP ADAPTER] Error consultando API externa de tipo de cambio ({}): {}", apiUrl, ex.getMessage());
            throw new DatoInvalidoException("No fue posible consultar la tasa de cambio externa para " + origen + " -> " + destino + ": " + ex.getMessage());
        }
    }

    @Override
    public BigDecimal convertir(BigDecimal monto, String monedaOrigen, String monedaDestino) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal tasa = obtenerTasaDeCambio(monedaOrigen, monedaDestino);
        return monto.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }
}
