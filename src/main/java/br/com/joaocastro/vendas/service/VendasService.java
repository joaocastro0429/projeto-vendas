package br.com.joaocastro.vendas.service;

import br.com.joaocastro.vendas.infra.entity.Vendas;
import br.com.joaocastro.vendas.infra.repository.VendasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendasService {
    private final VendasRepository vendasRepository;

    public VendasService(VendasRepository vendasRepository) {
        this.vendasRepository = vendasRepository;
    }

     public List<Vendas> listaVendas(){
        return vendasRepository.findAll();
     }
     public Vendas salvaVendas(Vendas vendas) {
        return vendasRepository.save(vendas);

     }
}
