package br.com.santander.santander.entity;

import br.com.santander.santander.enuns.Categoria;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String statusCpf;
    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Conta> contas = new ArrayList<Conta>();

    public Cliente() {

    }

    public Cliente(String nome, String cpf, LocalDate dataNascimento, Endereco endereco, Categoria categoria) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.categoria = categoria;
        this.contas = new ArrayList<Conta>();
    }


    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void addConta(Conta conta) {
        this.contas.add(conta);
        conta.setCliente(this);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCateroria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Conta buscarContaPorNumero(String numConta) {
        for (Conta conta : contas) {
            if (conta.getNumConta().equals(numConta)) {
                return conta;
            }
        }
        return null;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }

    public String getStatusCpf() {
        return statusCpf;
    }

    public void setStatusCpf(String statusCpf) {
        this.statusCpf = statusCpf;
    }

    public Cliente(Long idCliente, String nome, String cpf, LocalDate dataNascimento, String statusCpf,
                   Endereco endereco, Categoria categoria, List<Conta> contas) {
        super();
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.statusCpf = statusCpf;
        this.endereco = endereco;
        this.categoria = categoria;
        this.contas = contas;
    }


}
