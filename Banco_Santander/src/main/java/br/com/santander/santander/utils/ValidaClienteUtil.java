package br.com.santander.santander.utils;

import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Endereco;
import br.com.santander.santander.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class ValidaClienteUtil {

   private final ClienteRepository clienteRepository;

    public ValidaClienteUtil(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public static boolean validarNome(String nome) {
        return nome != null &&
                nome.length() >= 2 &&
                nome.length() <= 100 &&
                nome.matches("[a-zA-Z ]+");
    }


    public static boolean validarDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {
            return false;
        }
        return calcularIdade(dataNascimento) >= 18;
    }



    public static int calcularIdade(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        int idade = hoje.getYear() - dataNascimento.getYear();
        if (dataNascimento.getMonthValue() > hoje.getMonthValue() ||
                (dataNascimento.getMonthValue() == hoje.getMonthValue() &&
                        dataNascimento.getDayOfMonth() > hoje.getDayOfMonth())) {
            idade--;
        }
        return idade;
    }
    public boolean validarCpf(String cpf, boolean isAtualizar, Long clienteId) {

        if (!ValidaCpfUtil.isCPF(cpf)) {
            return false;
        }
        if (isAtualizar) {
            Cliente clienteExistente = clienteRepository.findByCpf(cpf);

            if (clienteExistente != null && !clienteExistente.getIdCliente().equals(clienteId)) {
                return false;
            }
        } else {
            Cliente clienteExistente = clienteRepository.findByCpf(cpf);
            if (clienteExistente != null) {
                return false;
            }
        }
        return true;
    }

    public boolean validarEndereco(Endereco endereco){
        return endereco != null &&
                endereco.getLogradouro() != null && !endereco.getLogradouro().isEmpty() &&
                endereco.getNumero() != null &&
                endereco.getBairro() != null && !endereco.getBairro().isEmpty() &&
                endereco.getLocalidade() != null && !endereco.getLocalidade().isEmpty();
    }

    public boolean validarCep(String cep){
        String regex = "^[0-9]{5}-[0-9]{3}$";
        return Pattern.matches(regex, cep);
    }
}


