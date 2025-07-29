package br.com.santander.santander.service;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Endereco;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



public interface ClienteService {

	void salvarCliente(Cliente cliente, boolean isAtualizar) throws Exception;

	Cliente buscarClientePorCpf(String cpf);

	List<Cliente> listarClientes();

	Optional<Cliente> findById(Long clienteId);

	void deletarCliente(Long clienteId);

	boolean validarCpf(String cpf, boolean isAtualizar, Long clienteId);

	boolean validarNome(String nome);

	boolean validarEndereco(Endereco endereco);

	boolean validarCEP(String cep);

	boolean validarDataNascimento(LocalDate dataNascimento);
}
