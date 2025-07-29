package br.com.santander.santander.service;

import java.util.Map;


public interface CambioService {


    public double obterCotacao(String moedaBase, String moedaDestino) throws Exception;

    // Método para converter o valor
    public double converterMoeda(double valor, String moedaBase, String moedaDestino) throws Exception;
    
    public Map<String, String> obterMoedasDisponiveis() throws Exception;

    

}
