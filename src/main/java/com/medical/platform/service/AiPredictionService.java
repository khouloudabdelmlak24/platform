package com.medical.platform.service;

import com.medical.platform.dto.PredictRequest;
import com.medical.platform.dto.PredictResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class AiPredictionService {

    private final RestClient restClient;

    public AiPredictionService(@Value("${ia.service.url:http://localhost:8001}") String iaServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(iaServiceUrl)
                .build();
    }

    /**
     * Appelle le service IA. Retourne null si le service est injoignable ou en erreur,
     * pour ne jamais empêcher l'enregistrement d'une mesure à cause de l'IA.
     */
    public PredictResponse predict(PredictRequest request) {
        try {
            return restClient.post()
                    .uri("/predict")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(PredictResponse.class);
        } catch (Exception e) {
            log.warn("Service IA injoignable ou en erreur : {}", e.getMessage());
            return null;
        }
    }
}