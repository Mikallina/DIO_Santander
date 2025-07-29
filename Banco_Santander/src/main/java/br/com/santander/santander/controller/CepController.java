package br.com.santander.santander.controller;

import br.com.santander.santander.entity.Endereco;

import br.com.santander.santander.service.CepService;
import br.com.santander.santander.service.impl.CepServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CepController {
	
	private final CepServiceImpl cepService;

    public CepController(CepService cepService) {
        this.cepService = (CepServiceImpl) cepService;
    }

    @GetMapping("/buscar-endereco/{cep}")
    public Endereco buscarEndereco(@PathVariable String cep) {
        return cepService.buscarEnderecoPorCep(cep);
    }

}
