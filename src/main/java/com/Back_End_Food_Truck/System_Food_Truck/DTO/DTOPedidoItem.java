package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTOPedidoItem {
    private Long produtoId;
    private DTOProduto produto;
    private Integer quantidade;
    private Double precoUnitario;
}
