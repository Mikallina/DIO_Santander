package br.com.santander.santander.repository;


import br.com.santander.santander.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	Cliente findByCpf(String cpf);

}
