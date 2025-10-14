package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ControllerProduto {

    @Autowired
    private ServiceProduto serviceProduto;

    @GetMapping("/lista")
    public List<DTOProduto> listarProdutos() {
        return serviceProduto.listarProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return serviceProduto.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar/{idCategoria}")
    public ResponseEntity<Produto> cadastrar(
            @RequestBody Produto produto,
            @PathVariable Long idCategoria) {
        produto = serviceProduto.criarProduto(produto, idCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/atualizar/{idProduto}/{idCategoria}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long idProduto,
            @PathVariable Long idCategoria,
            @RequestBody Produto produto) {

        return serviceProduto.atualizarProduto(idProduto, idCategoria, produto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deleted = serviceProduto.deletarProduto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/alternar-status/{id}")
    public ResponseEntity<Void> alternarStatusProduto(@PathVariable Long id) {
        boolean alterado = serviceProduto.alternarStatusProduto(id);

        if(alterado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
