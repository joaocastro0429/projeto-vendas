package br.com.joaocastro.vendas.controller;

import br.com.joaocastro.vendas.infra.dto.VendasRequest;
import br.com.joaocastro.vendas.infra.dto.VendasResponse;
import br.com.joaocastro.vendas.service.VendasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendasController {

    private final VendasService vendasService;

    public VendasController(VendasService vendasService) {
        this.vendasService = vendasService;
    }

    @GetMapping
    public List<VendasResponse> listarVendas() {
        return vendasService.listaVendas();
    }
    @PostMapping
    public VendasResponse salvarVendas(@RequestBody VendasRequest vendas) {
        return vendasService.salvaVendas(vendas);
    }
    @GetMapping("/{id}")
    public VendasResponse buscarVendaPorId(@PathVariable Long id) {
        return vendasService.buscarVendaPorId(id);

    }
    @PutMapping("/{id}")
    public VendasResponse atualizaVendas(
            @PathVariable("id") Long id,
            @RequestBody VendasRequest vendas) {

        return vendasService.atualizarVenda(id, vendas);
    }

    @GetMapping("/filtrar")
    public List<VendasResponse> filtrarVendas(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam("produto") String produto) {

        return vendasService.filtrarVendas(data, produto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaVendas(@PathVariable("id") Long id) {
        vendasService.deletaVendas(id);
    }
}
