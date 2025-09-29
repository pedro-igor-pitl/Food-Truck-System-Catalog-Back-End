package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DTOPedido {
    private LocalDateTime dataPedido;
    private String nomeUsuario;
    private String telefoneUsuario;
    private String emailUsuario;
    private DTOEndereco endereco;
    private String formaPagamento;
    private String observacao;
    private Double precoTotal;
    private List<DTOPedidoItem> itens;
}
