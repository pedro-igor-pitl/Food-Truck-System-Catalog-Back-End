package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceProduto {

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private RepositoryCategoria repositoryCategoria;

    public Produto criarProduto(DTOProduto dtoProduto, Long idCategoria) {
        Categoria categoria = repositoryCategoria.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = new Produto();
        produto.setNome(dtoProduto.getNome());
        produto.setDescricao(dtoProduto.getDescricao());
        produto.setPreco(dtoProduto.getPreco());
        produto.setImagemUrl(dtoProduto.getImagemUrl());
        produto.setAtivo(dtoProduto.getAtivo());
        produto.setCategoria(categoria);

        return repositoryProduto.save(produto);
    }


    public Optional<Produto> buscarPorId(Long id) {
        return repositoryProduto.findById(id);
    }

// Modifique a parte onde você mapeia os produtos para DTOProduto no seu serviço:

    public List<DTOProduto> listarProdutos() {
        List<Produto> produtos = repositoryProduto.findAll();

        return produtos.stream()
                .map(produto -> new DTOProduto(
                        produto.getId(),                // ID
                        produto.getNome(),              // Nome
                        produto.getDescricao(),         // Descrição
                        produto.getPreco(),             // Preço
                        produto.getImagemUrl(),         // URL da imagem
                        produto.getAtivo(),             // Status ativo
                        produto.getCategoria()          // Categoria (necessário)
                ))
                .collect(Collectors.toList());
    }



    // A lógica de atualização agora está no serviço, incluindo a manipulação da imagem
    public Optional<Produto> atualizarProduto(Long idProduto, Long idCategoria, String nome, String descricao, Double preco, Boolean ativo, MultipartFile imagem) {
        Optional<Produto> produtoBanco = repositoryProduto.findById(idProduto);

        if (produtoBanco.isPresent()) {
            Produto produtoAtualizado = produtoBanco.get();

            // Atualizando os dados do produto
            produtoAtualizado.setNome(nome);
            produtoAtualizado.setDescricao(descricao);
            produtoAtualizado.setPreco(preco);
            produtoAtualizado.setAtivo(ativo);

            // Processando a imagem, se fornecida
            if (imagem != null && !imagem.isEmpty()) {
                try {
                    String nomeImagem = UUID.randomUUID() + "_" + imagem.getOriginalFilename();

                    // Define o caminho onde a imagem será salva
                    Path caminhoImagem = Paths.get("src/main/resources/static/uploads/" + nomeImagem);

                    // Cria diretório se não existir
                    Files.createDirectories(caminhoImagem.getParent());

                    // Salva a imagem no disco
                    Files.copy(imagem.getInputStream(), caminhoImagem);

                    // Atualiza a URL da imagem no produto
                    produtoAtualizado.setImagemUrl("/uploads/" + nomeImagem);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Erro ao salvar a imagem.");
                }
            }

            // Atualizando a categoria do produto
            if (idCategoria != null) {
                Categoria categoria = repositoryCategoria.findById(idCategoria)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                produtoAtualizado.setCategoria(categoria);
            }

            // Salvando o produto atualizado
            repositoryProduto.save(produtoAtualizado);
            return Optional.of(produtoAtualizado);
        }

        return Optional.empty();
    }

    public boolean alternarStatusProduto(Long id) {
        Optional<Produto> produtoOptional = repositoryProduto.findById(id);

        if (!produtoOptional.isPresent()) {
            return false;
        }

        Produto produto = produtoOptional.get();

        boolean novoStatus = !produto.getAtivo();
        produto.setAtivo(novoStatus);

        repositoryProduto.save(produto);

        return true;
    }

    public boolean deletarProduto(Long id) {
        if (repositoryProduto.existsById(id)) {
            repositoryProduto.deleteById(id);
            return true;
        }
        return false;
    }
}
