package br.com.santander.santander.service.impl;

import br.com.santander.santander.entity.Endereco;
import br.com.santander.santander.service.CepService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepServiceImpl implements CepService {

    private final RestTemplate restTemplate;

    public CepServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Endereco buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, Endereco.class);
    }
}
