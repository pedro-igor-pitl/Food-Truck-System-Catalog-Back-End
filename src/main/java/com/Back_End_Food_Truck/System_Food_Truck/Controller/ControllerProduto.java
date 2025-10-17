package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping(value = "/cadastrar/{idCategoria}", consumes = "multipart/form-data")
    public ResponseEntity<DTOProduto> cadastrar(
            @PathVariable Long idCategoria,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") Double preco,
            @RequestParam("ativo") Boolean ativo,
            @RequestParam("imagem") MultipartFile imagem
    ) {
        try {
            // Criando um DTOProduto com os dados recebidos
            DTOProduto dtoProduto = new DTOProduto(nome, descricao, preco, ativo, "/uploads/" + imagem.getOriginalFilename());

            // Chama o serviço para salvar o produto
            Produto produtoSalvo = serviceProduto.criarProduto(dtoProduto, idCategoria);

            // Mapeando o produto salvo de volta para DTOProduto
            DTOProduto produtoDtoSalvo = new DTOProduto(
                    produtoSalvo.getId(),
                    produtoSalvo.getNome(),
                    produtoSalvo.getDescricao(),
                    produtoSalvo.getPreco(),
                    produtoSalvo.getImagemUrl(),
                    produtoSalvo.getAtivo(),
                    produtoSalvo.getCategoria()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoDtoSalvo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping(value = "/atualizar/{idProduto}/{idCategoria}", consumes = "multipart/form-data")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long idProduto,
            @PathVariable Long idCategoria,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") Double preco,
            @RequestParam("ativo") Boolean ativo,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        try {
            Optional<Produto> produtoAtualizado = serviceProduto.atualizarProduto(idProduto, idCategoria, nome, descricao, preco, ativo, imagem);

            return produtoAtualizado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deleted = serviceProduto.deletarProduto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/alternar-status/{id}")
    public ResponseEntity<Void> alternarStatusProduto(@PathVariable Long id) {
        boolean alterado = serviceProduto.alternarStatusProduto(id);

        if (alterado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

