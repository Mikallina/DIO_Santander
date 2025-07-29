package br.com.santander.santander.service;


import br.com.santander.santander.entity.Endereco;
import org.springframework.stereotype.Service;

public interface CepService {

      public Endereco buscarEnderecoPorCep(String cep) ;
}
