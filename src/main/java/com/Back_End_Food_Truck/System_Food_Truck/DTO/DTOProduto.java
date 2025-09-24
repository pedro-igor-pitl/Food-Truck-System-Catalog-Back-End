package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTOProduto {
    private String nome;
    private String descricao;
    private Double preco;
    private String categoria;
}