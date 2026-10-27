package br.com.joaocastro.vendas.infra.repository;

import br.com.joaocastro.vendas.infra.entity.Vendas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendasRepository extends JpaRepository<Vendas , Long> {
}
