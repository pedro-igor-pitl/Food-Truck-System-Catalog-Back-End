package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para entrada (request)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOProdutoRequest {
    private Boolean ativo;
    private String nome;
    private String descricao;
    private String imagem_url;
    private Double preco;
    private Long categoriaId; // usado para buscar no banco
}
