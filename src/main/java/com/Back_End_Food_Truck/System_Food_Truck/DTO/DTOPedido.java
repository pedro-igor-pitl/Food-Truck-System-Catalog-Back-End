package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DTOPedido {
    private String usuarioNome;
    private DTOEndereco endereco;
    private LocalDateTime dataPedido;
    private String formaPagamento;
    private String observacao;
    private Double precoTotal;
    private List<DTOPedidoItem> itens;
}
