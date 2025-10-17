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
    public ResponseEntity<Produto> cadastrar(
            @PathVariable Long idCategoria,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") Double preco,
            @RequestParam("ativo") Boolean ativo,
            @RequestParam("imagem") MultipartFile imagem
    ) {
        try {
            String nomeImagem = UUID.randomUUID() + "_" + imagem.getOriginalFilename();

            // Define o caminho
            //            // Gera um nome único para salvar a imagem
            Path caminhoImagem = Paths.get("src/main/resources/static/uploads/" + nomeImagem);

            // Cria diretório se não existir
            Files.createDirectories(caminhoImagem.getParent());

            // Salva a imagem no disco
            Files.copy(imagem.getInputStream(), caminhoImagem);

            // Monta o objeto Produto com os dados recebidos
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setAtivo(ativo);
            produto.setImagemUrl("/uploads/" + nomeImagem); // caminho que será salvo no banco

            // Chama o serviço para salvar o produto com a categoria associada
            Produto produtoSalvo = serviceProduto.criarProduto(produto, idCategoria);

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
