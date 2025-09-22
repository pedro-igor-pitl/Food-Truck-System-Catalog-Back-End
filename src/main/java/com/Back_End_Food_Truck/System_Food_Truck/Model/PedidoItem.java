package com.Back_End_Food_Truck.System_Food_Truck.Model;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;


    @Column(nullable = false)
    private Integer quantidade;


    @Column(nullable = false)
    private Double precoUnitario;
}