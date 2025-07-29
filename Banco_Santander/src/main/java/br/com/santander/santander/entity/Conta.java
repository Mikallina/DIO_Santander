package br.com.santander.santander.entity;


import br.com.santander.santander.enuns.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Conta {

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "idCliente")
    @JsonBackReference
    protected Cliente cliente;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idConta;

    protected int agencia;
    protected String numConta;
    protected double saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta")
    protected TipoConta tipoConta;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cartao> cartoes = new ArrayList<Cartao>();


    public Conta() {

    }

    public Conta(Cliente cliente, int agencia, String numConta) {
        this.cliente = cliente;
        this.agencia = agencia;
        this.numConta = numConta;
        this.saldo = 0;
    }


    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNumConta() {
        return numConta;
    }

    public void setNumConta(String numConta) {
        this.numConta = numConta;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }

    // Métodos abstratos que serão implementados nas subclasses
    public abstract void exibirSaldo();

    protected abstract double getSaldoTotal();

    protected abstract Conta[] values();

    public Conta(Cliente cliente, Long idConta, int agencia, String numConta, double saldo, TipoConta tipoConta,
                 List<Cartao> cartoes) {
        super();
        this.cliente = cliente;
        this.idConta = idConta;
        this.agencia = agencia;
        this.numConta = numConta;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.cartoes = cartoes;
    }

}