package br.com.joaocastro.vendas.service;

import br.com.joaocastro.vendas.infra.dto.VendasRequest;
import br.com.joaocastro.vendas.infra.dto.VendasResponse;
import br.com.joaocastro.vendas.infra.entity.Vendas;
import br.com.joaocastro.vendas.infra.repository.VendasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendasService {
    private final VendasRepository vendasRepository;

    public VendasService(VendasRepository vendasRepository) {
        this.vendasRepository = vendasRepository;
    }

     public List<VendasResponse> listaVendas(){
        List<VendasResponse> resposta = new ArrayList<>();
        for (Vendas venda : vendasRepository.findAll()) {
            resposta.add(new VendasResponse(venda.getId(), venda.getProduct(),
                    venda.getQuantity(), venda.getPrice(), venda.getSaleDate()));
        }
        return resposta;
     }
     public VendasResponse salvaVendas(VendasRequest vendas) {
        if (vendasRepository.existsByProductAndQuantityAndPriceAndSaleDate(
                vendas.product(),
                vendas.quantity(),
                vendas.price(),
                vendas.saleDate())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Venda já cadastrada");
        }
        Vendas venda = new Vendas();
        venda.setProduct(vendas.product());
        venda.setQuantity(vendas.quantity());
        venda.setPrice(vendas.price());
        venda.setSaleDate(vendas.saleDate());

        Vendas salva = vendasRepository.save(venda);
        return new VendasResponse(salva.getId(), salva.getProduct(),
                salva.getQuantity(), salva.getPrice(), salva.getSaleDate());

     }

     public VendasResponse buscarVendaPorId(Long id){
        Vendas venda = vendasRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,"Venda não encontrada"));
        return new VendasResponse(venda.getId(), venda.getProduct(),
                venda.getQuantity(), venda.getPrice(), venda.getSaleDate());
     }

    public VendasResponse atualizarVenda(Long id, VendasRequest dados) {
        Vendas venda = vendasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venda não encontrada"));

        venda.setProduct(dados.product());
        venda.setQuantity(dados.quantity());
        venda.setPrice(dados.price());
        venda.setSaleDate(dados.saleDate());

        Vendas atualizada = vendasRepository.save(venda);
        return new VendasResponse(atualizada.getId(), atualizada.getProduct(),
                atualizada.getQuantity(), atualizada.getPrice(), atualizada.getSaleDate());
    }

    public List<VendasResponse> filtrarVendas(LocalDate data, String produto) {
        List<VendasResponse> resposta = new ArrayList<>();
        for (Vendas venda : vendasRepository.findBySaleDateAndProduct(data, produto)) {
            resposta.add(new VendasResponse(venda.getId(), venda.getProduct(),
                    venda.getQuantity(), venda.getPrice(), venda.getSaleDate()));
        }
        return resposta;
    }
    public void deletaVendas(Long id) {
        Vendas venda = vendasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venda não encontrada"));

        vendasRepository.delete(venda);
    }
}
