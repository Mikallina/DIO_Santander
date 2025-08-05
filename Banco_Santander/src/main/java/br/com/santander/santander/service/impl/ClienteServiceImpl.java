package br.com.santander.santander.service.impl;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Endereco;
import br.com.santander.santander.repository.ClienteRepository;
import br.com.santander.santander.service.ClienteService;
import br.com.santander.santander.utils.ValidaClienteUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    private final ValidaClienteUtil validaClienteUtil;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ValidaClienteUtil validaClienteUtil) {
        this.clienteRepository = clienteRepository;
        this.validaClienteUtil = validaClienteUtil;
    }

    @Override
    public void salvarCliente(Cliente cliente, boolean isAtualizar) throws Exception {
        validarCliente(cliente, isAtualizar);
        clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    @Override
    public void deletarCliente(Long clienteId) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(clienteId);
        clienteExistente.ifPresent(cliente -> clienteRepository.deleteById(clienteId));
    }

    @Override
    public boolean validarCpf(String cpf, boolean isAtualizar, Long clienteId) {
        return validaClienteUtil.validarCpf(cpf, isAtualizar, clienteId);
    }

    @Override
    public boolean validarNome(String nome) {
        return validaClienteUtil.validarNome(nome);
    }

    @Override
    public boolean validarEndereco(Endereco endereco) {
        return validaClienteUtil.validarEndereco(endereco);
    }


    @Override
    public boolean validarDataNascimento(LocalDate dataNascimento) {
        return validaClienteUtil.validarDataNascimento(dataNascimento);
    }

    private void validarCliente(Cliente cliente, boolean isAtualizar) throws Exception {
        if (!validarCpf(cliente.getCpf(), isAtualizar, cliente.getIdCliente())) {
            throw new Exception("CPF inválido ou já cadastrado.");

        }
        if (!validarNome(cliente.getNome())) {
            throw new Exception("Nome inválido.");
        }
        if (!validarEndereco(cliente.getEndereco())) {
            throw new Exception("Endereço inválido.");
        }
        if (!validarDataNascimento(cliente.getDataNascimento())) {
            throw new Exception("Data de nascimento inválida.");
        }
    }

    private int calcularIdade(LocalDate dataNascimento) {
        return validaClienteUtil.calcularIdade(dataNascimento);
    }
}
