package br.com.santander.santander.repository;


import br.com.santander.santander.entity.Cartao;
import br.com.santander.santander.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByConta(Conta conta);

	Cartao findByNumCartao(String numCartao);
}
