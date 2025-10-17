package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DTOProduto {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private String imagemUrl;
    private Boolean ativo;
    private Categoria categoria;

    // Construtor adicional para quando não precisar do ID e categoria
    public DTOProduto(String nome, String descricao, Double preco, Boolean ativo, String imagemUrl) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.ativo = ativo;
        this.imagemUrl = imagemUrl;
    }
}
