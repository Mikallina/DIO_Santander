package br.com.santander.santander.dto;


import br.com.santander.santander.entity.Conta;

public class ContaResponseDTO {
	
	private String nome;
    private String cpf;
    private Conta conta;

    // Construtores, getters e setters

    public ContaResponseDTO(String nome, String cpf, Conta conta) {
        this.nome = nome;
        this.cpf = cpf;
        this.conta = conta;
    }

	public String getNome() {
		return nome;
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

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

    
    
}
