package br.com.joaocastro.vendas.infra.repository;

import br.com.joaocastro.vendas.infra.entity.Vendas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VendasRepository extends JpaRepository<Vendas , Long> {
    boolean existsByProductAndQuantityAndPriceAndSaleDate(String product, Integer quantity,
                                                          BigDecimal price, LocalDate saleDate);

    List<Vendas> findBySaleDateAndProduct(LocalDate saleDate, String product);
}
