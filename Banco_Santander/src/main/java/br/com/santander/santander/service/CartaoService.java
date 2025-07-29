package br.com.santander.santander.service;


import br.com.santander.santander.entity.Cartao;
import br.com.santander.santander.entity.Conta;
import br.com.santander.santander.enuns.Categoria;
import br.com.santander.santander.enuns.TipoCartao;
import java.time.LocalDate;
import java.util.List;


public interface CartaoService {

	void salvarCartao(Cartao cartao, boolean isAtualizar);

	Cartao criarCartao(Conta conta, TipoCartao tipoCartao, int senha, String diaVencimento, Categoria categoria);

	boolean alterarSenha(int senhaAntiga, int senhaNova, Cartao cartao);

	boolean alterarLimiteCartao(String numCartao, double novoLimite);

	boolean alterarStatus(String numCartao, boolean novoStatus);

	String gerarNumeroCartao(TipoCartao tipoCartao);

	boolean realizarCompra(String numCartao, double valor, LocalDate dataCompra);

	boolean realizarPagamentoFatura(String numCartao, double valorPagamento);

	Cartao buscarCartaoPorCliente(String numCartao);

	List<Cartao> buscarCartaoPorConta(Conta conta);

	Cartao buscarCartaoPorNumero(String numCartao);

	double consultarFatura(String numCartao);
}