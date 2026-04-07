package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCatalogo;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCatalogo {
    @Autowired
    private RepositoryProduto repositoryProduto;

    public List<DTOCatalogo> listarCatalogo() {

        List<Produto> produtos = repositoryProduto.findAll();


        return produtos.stream()
                .map(produto -> new DTOCatalogo(
                        produto.getId(),
                        produto.getCategoria().getNome(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getAtivo(),
                        produto.getImagemUrl()
                ))
                .collect(Collectors.toList());
    }
}
