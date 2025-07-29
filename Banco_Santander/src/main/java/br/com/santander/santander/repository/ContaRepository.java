package br.com.santander.santander.repository;


import br.com.santander.santander.entity.Cliente;
import br.com.santander.santander.entity.Conta;
import br.com.santander.santander.entity.ContaCorrente;
import br.com.santander.santander.enuns.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Conta findByNumConta(String numContaDestino);

    List<Conta> findByCliente(Cliente cliente);

    Optional<ContaCorrente> findByClienteAndTipoConta(Long idConta, TipoConta tipoConta, boolean conta);
}
