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
import java.nio.file.*;
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

    // 📁 Diretório base para uploads
    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");

    // ================================
    // CRIAR PRODUTO
    // ================================
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

    // ================================
    // BUSCAR POR ID
    // ================================
    public Optional<Produto> buscarPorId(Long id) {
        return repositoryProduto.findById(id);
    }

    // ================================
    // LISTAR PRODUTOS
    // ================================
    public List<DTOProduto> listarProdutos() {
        return repositoryProduto.findAll()
                .stream()
                .map(produto -> new DTOProduto(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getImagemUrl(),
                        produto.getAtivo(),
                        produto.getCategoria()
                ))
                .collect(Collectors.toList());
    }

    // ================================
    // ATUALIZAR PRODUTO
    // ================================
    public Optional<Produto> atualizarProduto(
            Long idProduto,
            Long idCategoria,
            String nome,
            String descricao,
            Double preco,
            Boolean ativo,
            MultipartFile imagem
    ) {

        Optional<Produto> produtoBanco = repositoryProduto.findById(idProduto);

        if (produtoBanco.isEmpty()) {
            return Optional.empty();
        }

        Produto produto = produtoBanco.get();

        // Atualizar dados básicos
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setAtivo(ativo);

        // ================================
        // ATUALIZAR IMAGEM (SE EXISTIR)
        // ================================
        if (imagem != null && !imagem.isEmpty()) {
            try {
                Files.createDirectories(uploadDir);

                // 🔥 Deleta imagem antiga
                if (produto.getImagemUrl() != null && produto.getImagemUrl().startsWith("/uploads/")) {
                    String nomeAntigo = produto.getImagemUrl().replace("/uploads/", "");
                    Path caminhoAntigo = uploadDir.resolve(nomeAntigo);
                    Files.deleteIfExists(caminhoAntigo);
                }

                // 🔥 Nova imagem
                String nomeImagem = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
                Path caminhoImagem = uploadDir.resolve(nomeImagem);

                Files.copy(imagem.getInputStream(), caminhoImagem, StandardCopyOption.REPLACE_EXISTING);

                produto.setImagemUrl("/uploads/" + nomeImagem);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao salvar imagem");
            }
        }

        // ================================
        // ATUALIZAR CATEGORIA
        // ================================
        if (idCategoria != null) {
            Categoria categoria = repositoryCategoria.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            produto.setCategoria(categoria);
        }

        repositoryProduto.save(produto);
        return Optional.of(produto);
    }

    // ================================
    // ALTERNAR STATUS
    // ================================
    public boolean alternarStatusProduto(Long id) {
        Optional<Produto> produtoOptional = repositoryProduto.findById(id);

        if (produtoOptional.isEmpty()) return false;

        Produto produto = produtoOptional.get();
        produto.setAtivo(!produto.getAtivo());

        repositoryProduto.save(produto);
        return true;
    }

    // ================================
    // DELETAR PRODUTO
    // ================================
    public boolean deletarProduto(Long id) {
        Optional<Produto> produtoOptional = repositoryProduto.findById(id);

        if (produtoOptional.isEmpty()) return false;

        Produto produto = produtoOptional.get();

        try {
            // 🔥 Deleta imagem se existir
            if (produto.getImagemUrl() != null && !produto.getImagemUrl().isEmpty()) {
                String nomeImagem = produto.getImagemUrl().replace("/uploads/", "");
                Path caminhoImagem = uploadDir.resolve(nomeImagem);
                Files.deleteIfExists(caminhoImagem);
            }

            repositoryProduto.deleteById(id);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar produto");
        }
    }
}