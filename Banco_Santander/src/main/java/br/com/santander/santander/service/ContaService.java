package br.com.santander.santander.service;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Conta;
import br.com.santander.santander.enuns.TipoConta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContaService {


    public void salvarConta(Cliente cliente, Conta conta, TipoConta tipoConta);

    List<Conta> buscarContasPorCliente(Cliente cliente);
    void listarContas();

    Conta criarConta(Cliente cliente, int agencia, TipoConta tipoConta);

     boolean realizarTransferenciaPoupanca(double valor, String numContaOrigem, String numContaDestino);

     boolean realizarTransferenciaPIX(double valor, String numContaOrigem, String chaveDestino);

     boolean realizarTransferenciaOutrasContas(double valor, String numContaOrigem, String numContaDestino);


     boolean realizarDeposito(String numContaDestino, double valor);


     boolean realizarTransferencia(double valor, String numContaOrigem, String numContaDestino,
                                         boolean transferenciaPoupança, boolean transferenciaPix, boolean transferenciaOutrasContas);

     boolean aplicarTaxaOuRendimento(Long idConta, TipoConta tipoConta, boolean conta);
     Conta buscarContas(String conta);

     Conta buscarContaPorClienteEConta(String cpf, String numConta);
}
