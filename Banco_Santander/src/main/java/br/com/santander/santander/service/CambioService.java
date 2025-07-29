package br.com.santander.santander.service;

import br.com.santander.santander.execptions.CambioException;

import java.util.Map;


public interface CambioService {


    public double obterCotacao(String moedaBase, String moedaDestino) throws CambioException;

    // Método para converter o valor
    public double converterMoeda(double valor, String moedaBase, String moedaDestino) throws Exception;
    
    public Map<String, String> obterMoedasDisponiveis() throws Exception;

    

}
