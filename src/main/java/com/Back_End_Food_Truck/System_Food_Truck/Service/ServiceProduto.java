package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProdutoRequest;
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

    public Produto criarProduto(DTOProduto dto, Long idCategoria) {
        Categoria categoria = repositoryCategoria.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = new Produto();
        produto.setAtivo(dto.getAtivo());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setImagemUrl(dto.getImagem_url());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(categoria);

        return repositoryProduto.save(produto);
    }


    // LISTAR todos os produtos
    public List<DTOProduto> listarProdutos() {
        return repositoryProduto.findAll().stream()
                .map(p -> new DTOProduto(
                        p.getAtivo(),
                        p.getNome(),
                        p.getDescricao(),
                        p.getImagemUrl(),
                        p.getPreco(),
                        p.getCategoria() != null ? p.getCategoria().getDescricao() : null,
                        p.getCategoria() != null ? p.getCategoria().getAtivo() : null,
                        p.getCategoria() != null ? p.getCategoria().getNome() : null
                ))
                .collect(Collectors.toList());
    }

    // BUSCAR por ID
    public Optional<DTOProduto> buscarPorId(Long id) {
        return repositoryProduto.findById(id)
                .map(p -> new DTOProduto(
                        p.getAtivo(),
                        p.getNome(),
                        p.getDescricao(),
                        p.getImagemUrl(),
                        p.getPreco(),
                        p.getCategoria() != null ? p.getCategoria().getDescricao() : null,
                        p.getCategoria() != null ? p.getCategoria().getAtivo() : null,
                        p.getCategoria() != null ? p.getCategoria().getNome() : null
                ));
    }

    public Optional<Produto> atualizarProduto(Long idProduto, Long idCategoria, DTOProduto dtoProduto) {
        Optional<Produto> produtoBanco = repositoryProduto.findById(idProduto);

        if (produtoBanco.isPresent()) {
            Produto produto = produtoBanco.get();
            produto.setNome(dtoProduto.getNome());
            produto.setDescricao(dtoProduto.getDescricao());
            produto.setAtivo(dtoProduto.getAtivo());
            produto.setImagemUrl(dtoProduto.getImagem_url());
            produto.setPreco(dtoProduto.getPreco());

            if (idCategoria != null) {
                Categoria categoria = repositoryCategoria.findById(idCategoria)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                produto.setCategoria(categoria);
            }

            repositoryProduto.save(produto);
            return Optional.of(produto);
        }

        return Optional.empty();
    }


    // DELETAR produto
    public boolean deletarProduto(Long id) {
        if (repositoryProduto.existsById(id)) {
            repositoryProduto.deleteById(id);
            return true;
        }
        return false;
    }
}
