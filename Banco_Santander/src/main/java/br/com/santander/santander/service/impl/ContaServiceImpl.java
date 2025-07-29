package br.com.santander.santander.service.impl;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Conta;
import br.com.santander.santander.entity.ContaCorrente;
import br.com.santander.santander.entity.ContaPoupanca;
import br.com.santander.santander.enuns.TipoConta;
import br.com.santander.santander.repository.ClienteRepository;
import br.com.santander.santander.repository.ContaRepository;
import br.com.santander.santander.service.ContaService;
import br.com.santander.santander.utils.ContaUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;

    private final ClienteRepository clienteRepository;

    private final ContaUtil contaUtil;

    double taxaRendimento = 0;

    public ContaServiceImpl(ContaRepository contaRepository, ClienteRepository clienteRepository, ContaUtil contaUtil) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.contaUtil = contaUtil;
    }


    @Override
    public void salvarConta(Cliente cliente, Conta conta, TipoConta tipoConta) {
        if (conta.getNumConta() == null) {
            throw new IllegalArgumentException("Erro: O número da conta é obrigatório.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Erro: Cliente inválido");
        }

        if (conta instanceof ContaCorrente) {
            contaUtil.taxaManutencaoCC(cliente, tipoConta, (ContaCorrente) conta);
        }

        if (conta instanceof ContaPoupanca) {
            contaUtil.taxaManutencaoCP(cliente, tipoConta, (ContaPoupanca) conta);
        }

        contaRepository.save(conta);

        System.out.println("Conta salva com sucesso: " + conta.getNumConta());
    }


    @Override
    public List<Conta> buscarContasPorCliente(Cliente cliente) {
        return contaRepository.findByCliente(cliente);
    }

    @Override
    public void listarContas() {
        if (contaRepository.findAll().isEmpty()) {
            System.out.println("Nenhuma conta cadastrada");
        } else {
            for (Conta conta : contaRepository.findAll()) {
                System.out.println("Conta: " + conta.getNumConta());
            }
        }
    }



    @Override
    public Conta criarConta(Cliente cliente, int agencia, TipoConta tipoConta) {
        String numConta = contaUtil.gerarNumeroConta(agencia, tipoConta);
        Conta conta;

        if (tipoConta == TipoConta.CORRENTE) {
            conta = new ContaCorrente(cliente, agencia, numConta, tipoConta);
        } else if (tipoConta == TipoConta.POUPANCA) {
            conta = new ContaPoupanca(cliente, agencia, numConta, tipoConta);
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido.");
        }

        return contaRepository.save(conta);
    }




    @Override
    public boolean realizarTransferenciaPoupanca(double valor, String numContaOrigem, String numContaDestino) {
        return realizarTransferencia(valor, numContaOrigem, numContaDestino, true, false, false);
    }

    @Override
    public boolean realizarTransferenciaPIX(double valor, String numContaOrigem, String chaveDestino) {
        return realizarTransferencia(valor, numContaOrigem, chaveDestino, false, true, false);
    }

    @Override
    public boolean realizarTransferenciaOutrasContas(double valor, String numContaOrigem, String numContaDestino) {
        return realizarTransferencia(valor, numContaOrigem, numContaDestino, false, false, true);
    }

    @Override
    public boolean realizarDeposito(String numContaDestino, double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do depósito não pode ser zero");
        }

        Conta conta = contaRepository.findByNumConta(numContaDestino);
        System.out.println("Número da conta recebido no backend: " + numContaDestino);
        if (conta == null) {
            throw new RuntimeException("Conta Não encontrada");
        }

        double novoSaldo = conta.getSaldo() + valor;
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);

        return true;

    }

    @Override
    public boolean realizarTransferencia(double valor, String numContaOrigem, String numContaDestino, boolean transferenciaPoupança, boolean transferenciaPix, boolean transferenciaOutrasContas) {

            Conta contaOrigem = contaRepository.findByNumConta(numContaOrigem);
            Conta contaDestino = contaRepository.findByNumConta(numContaDestino);

            if (contaOrigem == null) {
                throw new IllegalArgumentException("Conta de origem não encontrada.");
            }

            if (valor <= 0) {
                throw new IllegalArgumentException("Valor não pode ser zero ou negativo.");
            }

            if (valor > contaOrigem.getSaldo()) {
                throw new IllegalArgumentException("Saldo insuficiente.");
            }

            if (transferenciaPoupança || transferenciaOutrasContas) {
                if (contaDestino == null) {
                    throw new IllegalArgumentException("Conta de destino não encontrada.");
                }
                contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
                contaDestino.setSaldo(contaDestino.getSaldo() + valor);
            }

            if (transferenciaPix) {
                contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            }

            contaRepository.save(contaOrigem);

            // 	Se houve alguma modificação na conta de destino, salvar também
            if (contaDestino != null && (transferenciaPoupança || transferenciaOutrasContas)) {
                contaRepository.save(contaDestino);
            }

            return true;
        }

    @Override
    public boolean aplicarTaxaOuRendimento(Long idConta, TipoConta tipoConta, boolean conta) {
        Optional<ContaCorrente> contaCorrenteOpt = contaRepository.findByClienteAndTipoConta(idConta,tipoConta,conta);
        if (contaCorrenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        ContaCorrente contaCorrente = contaCorrenteOpt.get();

        // Buscar cliente
        Cliente cliente = contaCorrente.getCliente(); // ou usar clienteRepository.findById() se necessário

        // Aplica taxa de manutenção
        double taxa = contaUtil.taxaManutencaoCC(cliente, tipoConta, contaCorrente);

        // Atualiza a conta com a taxa aplicada
        contaCorrente.setSaldo(contaCorrente.getSaldo() - taxa);
        contaRepository.save(contaCorrente);

        return true;

    }


    @Override
    public Conta buscarContas(String conta) {
        return contaRepository.findByNumConta(conta);
    }

    @Override
    public Conta buscarContaPorClienteEConta(String cpf, String numConta) {
        Cliente cliente = clienteRepository.findByCpf(cpf);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        // Busca as contas do cliente
        List<Conta> contas = contaRepository.findByCliente(cliente);

        // Filtra a conta com base no número da conta
        return contas.stream().filter(c -> c.getNumConta().equals(numConta)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
    }
}
