package br.com.joaocastro.vendas.infra.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendasResponse(
        Long id,
        String product,
        Integer quantity,
        BigDecimal price,
        LocalDate saleDate
) {
}
