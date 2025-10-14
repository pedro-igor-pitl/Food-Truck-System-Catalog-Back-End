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
}
