package br.com.santander.santander.service.impl;

import br.com.santander.santander.entity.Cartao;
import br.com.santander.santander.entity.CartaoCredito;
import br.com.santander.santander.entity.CartaoDebito;
import br.com.santander.santander.entity.Conta;
import br.com.santander.santander.enuns.Categoria;
import br.com.santander.santander.enuns.TipoCartao;
import br.com.santander.santander.repository.CartaoRepository;
import br.com.santander.santander.repository.ContaRepository;
import br.com.santander.santander.service.CartaoService;
import br.com.santander.santander.utils.CartaoUtil;
import br.com.santander.santander.utils.CategoriaLimiteUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class CartaoServiceImpl  implements CartaoService {

    private final CartaoRepository cartaoRepository;
    private final ContaRepository contaRepository;
    private final CartaoUtil cartaoUtil;
    private final CategoriaLimiteUtil categoriaLimiteUtil;


    public CartaoServiceImpl(CartaoRepository cartaoRepository, ContaRepository contaRepository, CartaoUtil cartaoUtil, CategoriaLimiteUtil categoriaLimiteUtil) {
        this.cartaoRepository = cartaoRepository;
        this.contaRepository = contaRepository;
        this.cartaoUtil = cartaoUtil;
        this.categoriaLimiteUtil = categoriaLimiteUtil;
    }


    @Override
    public void salvarCartao(Cartao cartao, boolean isAtualizar) {
        if (cartao == null || cartao.getNumCartao() == null) {
            throw new IllegalArgumentException("Erro: Tentativa de Salvar o Cartão nulo");
        }
        if (isAtualizar) {

            cartaoRepository.save(cartao);
            System.out.println("Cartão atualizado: " + cartao.getNumCartao());
        } else {
            cartaoRepository.save(cartao);
            System.out.println("Novo cartão adicionado: " + cartao.getNumCartao());
        }
    }

    @Override
    public Cartao criarCartao(Conta conta, TipoCartao tipoCartao, int senha, String diaVencimento, Categoria categoria) {
        if (conta == null) {
            throw new IllegalArgumentException("Erro: Cliente não pode ser null.");
        }
        LocalDate dataVencimento = null;
        if (tipoCartao == TipoCartao.CREDITO) {
            if (diaVencimento == null) {
                throw new IllegalArgumentException("Erro: O dia de vencimento é obrigatório para cartões de crédito.");
            }
            int dia = Integer.parseInt(diaVencimento);
            LocalDate dataAtual = LocalDate.now();
            int anoAtual = dataAtual.getYear();
            int mesAtual = dataAtual.getMonthValue();

            try {
                dataVencimento = LocalDate.of(anoAtual, mesAtual, dia);
            } catch (Exception e) {
                System.out.println("Data inválida" + e.getMessage());
            }

            if (dataVencimento.isBefore(dataAtual)) {
                if (mesAtual == 12) {
                    dataVencimento = LocalDate.of(anoAtual + 1, 1, dia);
                } else {
                    dataVencimento = LocalDate.of(anoAtual, mesAtual + 1, dia);
                }
            }
        }

        Cartao cartao;
        String numCartao = gerarNumeroCartao(tipoCartao);

        if (tipoCartao == TipoCartao.CREDITO) {
            cartao = new CartaoCredito(conta, senha, numCartao, TipoCartao.CREDITO, categoriaLimiteUtil.limiteCredito(categoria),
                    diaVencimento, dataVencimento);
        } else if (tipoCartao == TipoCartao.DEBITO) {
            cartao = new CartaoDebito(conta, numCartao, TipoCartao.DEBITO, senha, categoriaLimiteUtil.limiteDiario(categoria));
        } else {
            throw new IllegalArgumentException("Tipo de cartão inválido.");
        }

        cartaoRepository.save(cartao);

        return cartao;    }

    @Override
    public boolean alterarSenha(int senhaAntiga, int senhaNova, Cartao cartao) {
        if (!cartao.isStatus()) {
            throw new IllegalArgumentException("Status do Cartão Desativado");
        }
        if (cartao.getSenha() == senhaAntiga) {
            cartao.setSenha(senhaNova);
            System.out.println("Senha atualizada com sucesso.");
            cartaoRepository.save(cartao);
            return true;
        } else {
            System.out.println("Senha antiga incorreta.");
            return false;
        }

    }

    @Override
    public boolean alterarLimiteCartao(String numCartao, double novoLimite){
        Optional<Cartao> cartaoOptional = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));

        if (cartaoOptional.isPresent()) {
            Cartao cartao = cartaoOptional.get();
            System.out.println("Cartão Encontrado: " + cartao.getNumCartao());

            if (!cartao.isStatus()) {
                throw new IllegalArgumentException("Status do Cartão Desativado");
            }

            if (cartao instanceof CartaoCredito) {
                CartaoCredito cartaoCredito = (CartaoCredito) cartao;
                cartaoCredito.alterarLimiteCredito(novoLimite);
                cartaoRepository.save(cartaoCredito);
                System.out.println("Limite de crédito atualizado para: R$ " + novoLimite);
                return true;
            } else if (cartao instanceof CartaoDebito) {
                CartaoDebito cartaoDebito = (CartaoDebito) cartao;
                cartaoDebito.alterarLimiteDebito(novoLimite);
                cartaoRepository.save(cartaoDebito);
                System.out.println("Limite de débito atualizado para: R$ " + novoLimite);
                return true;
            } else {
                System.out.println("O cartão não é de crédito nem de débito.");
            }
        } else {
            System.out.println("Cartão não encontrado.");
        }
        return false;
    }

    @Override
    public boolean alterarStatus(String numCartao, boolean novoStatus) {
        Optional<Cartao> cartaoOptional = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));
        if (cartaoOptional.isPresent()) {
            Cartao cartao = cartaoOptional.get();
            cartao.setStatus(novoStatus);
            cartaoRepository.save(cartao);
            System.out.println("Status do cartão de número " + cartao.getNumCartao() + " alterado para " + novoStatus);
            return true;
        } else {
            throw new IllegalArgumentException("Cartão não encontrado com o ID fornecido.");
        }
    }

    @Override
    public String gerarNumeroCartao(TipoCartao tipoCartao) {
        String numeroParcial = cartaoUtil.gerarNumeroAleatorio(15);
        int digitoVerificador = cartaoUtil.calcularDigitoLuhn(numeroParcial);
        return numeroParcial + digitoVerificador;
    }

    @Override
    public boolean realizarCompra(String numCartao, double valor, LocalDate dataCompra){
        if (valor <= 0) {
            System.out.println("O valor do pagamento deve ser positivo.");
            return false;
        }
        Cartao cartao = cartaoRepository.findByNumCartao(numCartao);

        if (cartao == null) {
            throw new RuntimeException("Cartão Não encontrado");
        }
        if (!cartao.isStatus()) {
            throw new IllegalArgumentException("Status do Cartão Desativado");
        }

        if (cartao instanceof CartaoCredito) {
            CartaoCredito cartaoCredito = (CartaoCredito) cartao;

            if (cartaoCredito.getLimiteCredito() > valor) {
                cartaoCredito.setSaldoMes(cartaoCredito.getSaldoMes() + valor);
                cartaoCredito.setSaldoCredito(cartaoCredito.getLimiteCredito() - valor);
                cartaoCredito.setPagamento(cartaoCredito.getPagamento() + valor);

                cartaoCredito.setDataCompra(dataCompra);
                salvarCartao(cartaoCredito, true);

                System.out.println("Pagamento realizado com sucesso.");
                return true;
            } else {
                System.out.println("Limite de crédito excedido.");
                return false;
            }
        } else {
            System.out.println("Este cartão não é de crédito.");
            return false;
        }
    }


    @Override
    public boolean realizarPagamentoFatura(String numCartao, double valorPagamento){
        Cartao cartao = cartaoRepository.findByNumCartao(numCartao);

        if (cartao == null || !(cartao instanceof CartaoCredito)) {
            throw new RuntimeException("Cartão de crédito não encontrado ou tipo de cartão inválido.");
        }

        if (!cartao.isStatus()) {
            throw new IllegalArgumentException("Status do Cartão Desativado");
        }

        CartaoCredito cartaoCredito = (CartaoCredito) cartao;
        Conta conta = cartaoCredito.getConta();

        if (conta == null) {
            throw new RuntimeException("Conta associada ao cartão não encontrada.");
        }

        if (valorPagamento <= 0 || valorPagamento > cartaoCredito.getSaldoMes()) {
            throw new RuntimeException("Valor do pagamento inválido.");
        }

        if (conta.getSaldo() < valorPagamento) {
            throw new RuntimeException("Saldo insuficiente na conta para pagar a fatura.");
        }

        // Debita da conta corrente
        conta.setSaldo(conta.getSaldo() - valorPagamento);

        // Realiza o pagamento da fatura no cartão
        boolean pagamentoEfetuado = cartaoCredito.pagarFatura(valorPagamento);

        if (!pagamentoEfetuado) {
            throw new RuntimeException("Não foi possível realizar o pagamento da fatura.");
        }

        // Salva alterações no cartão e conta
        salvarCartao(cartaoCredito, true);
        contaRepository.save(conta);

        return true;
    }

    @Override
    public Cartao buscarCartaoPorCliente(String numCartao) {
        Optional<Cartao> cartoes = Optional.ofNullable(cartaoRepository.findByNumCartao(numCartao));
        if (cartoes != null && !cartoes.isEmpty()) {
            return cartoes.get();
        }
        return null;
    }

    @Override
    public List<Cartao> buscarCartaoPorConta(Conta conta) {
        return cartaoRepository.findByConta(conta);
    }

    @Override
    public Cartao buscarCartaoPorNumero(String numCartao) {
        return cartaoRepository.findByNumCartao(numCartao);
    }

    @Override
    public double consultarFatura(String numCartao) {
        Cartao cartao = cartaoRepository.findByNumCartao(numCartao);

        if (cartao == null) {
            throw new IllegalArgumentException("Cartão não encontrado");
        }

        if (cartao instanceof CartaoCredito) {

            CartaoCredito cartaoCredito = (CartaoCredito) cartao;

            return cartaoCredito.getSaldoMes();
        } else {

            throw new IllegalArgumentException("Cartão não é de crédito");
        }
    }

}
