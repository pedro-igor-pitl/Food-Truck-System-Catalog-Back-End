package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Optional<Produto> atualizarProduto(Long idProduto, Long idCategoria, String nome, String descricao, Double preco, Boolean ativo, MultipartFile imagem) {
        Optional<Produto> produtoBanco = repositoryProduto.findById(idProduto);

        if (produtoBanco.isPresent()) {
            Produto produtoAtualizado = produtoBanco.get();

            // Atualizando os dados básicos do produto
            produtoAtualizado.setNome(nome);
            produtoAtualizado.setDescricao(descricao);
            produtoAtualizado.setPreco(preco);
            produtoAtualizado.setAtivo(ativo);

            // Se uma nova imagem for fornecida, substituímos a imagem antiga
            if (imagem != null && !imagem.isEmpty()) {
                try {
                    // 1. Remover a imagem anterior, se houver
                    if (produtoAtualizado.getImagemUrl() != null && !produtoAtualizado.getImagemUrl().isEmpty()) {
                        String imagemAntigaUrl = produtoAtualizado.getImagemUrl();
                        String nomeImagemAntiga = imagemAntigaUrl.replace("/uploads/", "");
                        Path caminhoImagemAntiga = Paths.get("src/main/resources/static/uploads", nomeImagemAntiga);

                        // Deletar a imagem antiga
                        Files.deleteIfExists(caminhoImagemAntiga); // Deleta a imagem antiga, se existir
                    }

                    // 2. Gerar um nome único para a nova imagem
                    String nomeImagem = UUID.randomUUID() + "_" + imagem.getOriginalFilename();

                    // 3. Definir o caminho onde a nova imagem será salva
                    Path caminhoImagem = Paths.get("src/main/resources/static/uploads", nomeImagem);

                    // 4. Criar diretórios se não existirem
                    Files.createDirectories(caminhoImagem.getParent());

                    // 5. Salvar a nova imagem no disco
                    Files.copy(imagem.getInputStream(), caminhoImagem);

                    // 6. Atualizar a URL da nova imagem no produto
                    produtoAtualizado.setImagemUrl("/uploads/" + nomeImagem);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Erro ao salvar a imagem.");
                }
            }

            // Atualizando a categoria do produto, se fornecido um ID de categoria
            if (idCategoria != null) {
                Categoria categoria = repositoryCategoria.findById(idCategoria)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                produtoAtualizado.setCategoria(categoria);
            }

            // Salvando o produto atualizado
            repositoryProduto.save(produtoAtualizado);
            return Optional.of(produtoAtualizado);
        }

        return Optional.empty(); // Caso o produto não seja encontrado
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
        Optional<Produto> produtoOptional = repositoryProduto.findById(id);

        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();

            String imagemUrl = produto.getImagemUrl();

            if(imagemUrl != null && !imagemUrl.isEmpty()) {
                String nomeImagem = imagemUrl.replace("/uploads", "");

                Path caminhoImagem = Paths.get("src/main/resources/static/uploads", nomeImagem);

                try {
                    Files.deleteIfExists(caminhoImagem);

                    repositoryProduto.findById(id);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }else {
                repositoryProduto.deleteById(id);
                return true;
                }
            }
        return false;
    }
}
