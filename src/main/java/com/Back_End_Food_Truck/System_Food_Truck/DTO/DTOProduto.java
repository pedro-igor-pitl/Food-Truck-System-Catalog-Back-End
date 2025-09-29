package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOProduto {
    private Boolean ativo;
    private String nome;
    private String descricao;
    private String imagem_url;
    private Double preco;
    private String descricaoCategoria;
    private Boolean ativoCategoria;
    private String nomeCategoria;

    // Método para converter o DTO em Produto
    public Produto toModel() {
        Produto produto = new Produto();
        produto.setAtivo(this.ativo);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setImagemUrl(this.imagem_url);
        produto.setPreco(this.preco);

        Categoria categoria = new Categoria();
        categoria.setDescricao(this.descricaoCategoria);
        categoria.setAtivo(this.ativoCategoria);
        categoria.setNome(this.nomeCategoria);

        produto.setCategoria(categoria);
        return produto;
    }
}
