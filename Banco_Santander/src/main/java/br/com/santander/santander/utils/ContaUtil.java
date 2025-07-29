package br.com.santander.santander.utils;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.ContaCorrente;
import br.com.santander.santander.entity.ContaPoupanca;
import br.com.santander.santander.enuns.TipoConta;
import org.springframework.stereotype.Service;

@Service
public class ContaUtil {

    double taxaRendimento;
    public String gerarNumeroConta(int agencia, TipoConta tipoConta) {
        StringBuilder conta = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            conta.append((int) (Math.random() * 10));
        }

        // Calcular o dígito verificador (8º número) usando módulo 11
        int soma = 0;
        for (int i = 0; i < conta.length(); i++) {
            soma += (conta.charAt(i) - '0') * (i + 2);
        }

        int dv = soma % 11;
        if (dv == 10) {
            dv = 0;
        }

        conta.append(dv);

        String tipoContaString = tipoConta.getTipoAbreviado();
        return String.format("%s-%04d-%s", tipoContaString, agencia, conta.toString());
    }
    public double taxaManutencaoCC(Cliente cliente, TipoConta tipoConta, ContaCorrente contaC) {
        double taxaManutencao = 0;

        if (cliente.getCategoria().getDescricao().equals("Comum")) {

            taxaManutencao = 12;

        } else if (cliente.getCategoria().getDescricao().equals("Super")) {

            taxaManutencao = 8;

        } else {
            taxaManutencao = 2;
        }

        if (contaC.getSaldo() < taxaManutencao) {
            throw new IllegalArgumentException("Saldo insuficiente para aplicar a taxa de manutenção");
        }

        contaC.setTaxaManutencao(taxaManutencao);
        return taxaManutencao;
    }
    public double taxaManutencaoCP(Cliente cliente, TipoConta tipoConta, ContaPoupanca contaP) {
        double saldoAtual = contaP.getSaldo();

        if (cliente.getCategoria().getDescricao().equals("Comum")) {

            taxaRendimento = 0.5;

        } else if (cliente.getCategoria().getDescricao().equals("Super")) {

            taxaRendimento = 0.7;

        } else {
            taxaRendimento = 0.9;
        }

        double taxaMensal = taxaRendimento / 12;
        double saldoRendimento = saldoAtual * Math.pow(1 + (taxaMensal / 100), 1);
        double rendimentoMensal = saldoRendimento - saldoAtual;

        contaP.setTaxaRendimento(taxaRendimento);

        System.out.println("Saldo atual: " + saldoAtual);
        System.out.println("Taxa mensal: " + taxaMensal);
        System.out.println("Rendimento mensal: " + rendimentoMensal);

        return rendimentoMensal;
    }
}
