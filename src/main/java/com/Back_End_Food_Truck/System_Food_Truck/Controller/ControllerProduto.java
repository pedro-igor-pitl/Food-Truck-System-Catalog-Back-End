package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProdutoRequest;
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

    // LISTAR todos os produtos
    @GetMapping("/lista")
    public List<DTOProduto> listarProdutos() {
        return serviceProduto.listarProdutos();
    }

    // BUSCAR por ID
    @GetMapping("/{id}")
    public ResponseEntity<DTOProduto> buscarPorId(@PathVariable Long id) {
        return serviceProduto.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CRIAR produto
    @PostMapping("/cadastrar/{idCategoria}")
    public ResponseEntity<Produto> cadastrar(
            @RequestBody DTOProduto dto,
            @PathVariable Long idCategoria) {
        Produto produto = serviceProduto.criarProduto(dto, idCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }


    // ATUALIZAR produto
    @PutMapping("/atualizar/{idProduto}/{idCategoria}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long idProduto,
            @PathVariable Long idCategoria,
            @RequestBody DTOProduto dtoProduto) {

        return serviceProduto.atualizarProduto(idProduto, idCategoria, dtoProduto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // DELETAR produto
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deleted = serviceProduto.deletarProduto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
