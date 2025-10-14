package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceProduto {

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private RepositoryCategoria repositoryCategoria;

    public Produto criarProduto(Produto produto, Long idCategoria) {
        Categoria categoria = repositoryCategoria.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produto.setAtivo(produto.getAtivo());
        produto.setNome(produto.getNome());
        produto.setDescricao(produto.getDescricao());
        produto.setImagemUrl(produto.getImagemUrl());
        produto.setPreco(produto.getPreco());
        produto.setCategoria(categoria);

        return repositoryProduto.save(produto);
    }

    public List<DTOProduto> listarProdutos() {
        List<Produto> produtos = repositoryProduto.findAll();

        return produtos.stream()
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

    public Optional<Produto> buscarPorId(Long id) {
        return repositoryProduto.findById(id);
    }

    public Optional<Produto> atualizarProduto(Long idProduto, Long idCategoria, Produto produto) {
        Optional<Produto> produtoBanco = repositoryProduto.findById(idProduto);

        if (produtoBanco.isPresent()) {
            Produto produtoAtualizado = produtoBanco.get();

            produtoAtualizado.setNome(produto.getNome());
            produtoAtualizado.setDescricao(produto.getDescricao());
            produtoAtualizado.setAtivo(produto.getAtivo());
            produtoAtualizado.setImagemUrl(produto.getImagemUrl());
            produtoAtualizado.setPreco(produto.getPreco());

            if (idCategoria != null) {
                Categoria categoria = repositoryCategoria.findById(idCategoria)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                produtoAtualizado.setCategoria(categoria);
            }

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
