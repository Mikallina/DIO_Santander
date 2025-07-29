package br.com.santander.santander.service.impl;

import br.com.santander.santander.service.CambioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CambioServiceImpl implements CambioService {

    private final RestTemplate restTemplate;

    public CambioServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${api.cambio.url}")
    private String apiUrl;

    @Value("${api.cambio.key}")
    private String apiKey;

    @Override
    public double obterCotacao(String moedaBase, String moedaDestino) throws Exception {
        try {

            String url = String.format("%s/%s/latest/%s", apiUrl, apiKey, moedaBase);

            System.out.println("URL gerada: " + url);

            // Requisição para obter a resposta da API
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

            // Obtenha o corpo da resposta
            Map<String, Object> responseBody = response.getBody();

            // Verifique se a resposta contém a chave "conversion_rates"
            if (responseBody == null || !responseBody.containsKey("conversion_rates")) {
                throw new Exception("A resposta da API não contém a chave 'conversion_rates'.");
            }

            // Acessar o mapa de taxas de câmbio (conversion_rates)
            Map<String, Double> conversionRates = (Map<String, Double>) responseBody.get("conversion_rates");

            // Verifique se a moeda de destino existe no mapa de taxas
            Double cotacao = conversionRates.get(moedaDestino);

            if (cotacao == null) {
                throw new Exception("Moeda de destino não encontrada.");
            }

            return cotacao;

        }catch (Exception e){
            throw new Exception("Erro ao buscar as cotacao: " + e);

        }

    }


    @Override
    public double converterMoeda(double valor, String moedaBase, String moedaDestino) throws Exception {
        double cotacao = obterCotacao(moedaBase, moedaDestino);
        return valor * cotacao;
    }

    @Override
    public Map<String, String> obterMoedasDisponiveis() throws Exception {
        try {
            // URL corrigida para pegar as taxas de câmbio para USD
            String url = String.format("%s/%s/latest/USD", apiUrl, apiKey); // Certifique-se de que a URL está correta

            // Requisição para obter a resposta da API
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

            // Obter o corpo da resposta
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("conversion_rates")) {
                // Debug: Imprimir a resposta da API
                System.out.println("Resposta da API: " + responseBody);
                throw new Exception("A resposta da API não contém a chave 'conversion_rates'.");
            }

            // Acessar a chave 'conversion_rates' que contém as moedas e suas taxas
            Map<String, String> conversionRates = (Map<String, String>) responseBody.get("conversion_rates");
            System.out.println(conversionRates);
            return conversionRates;

        } catch (Exception e) {
            // Log de erro

            throw new Exception("Erro ao buscar as moedas: " + e.getMessage());
        }
    }

}
