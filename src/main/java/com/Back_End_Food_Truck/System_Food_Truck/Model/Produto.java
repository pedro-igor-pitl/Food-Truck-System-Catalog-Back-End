package com.Back_End_Food_Truck.System_Food_Truck.Model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;


    private String descricao;


    @Column(nullable = false)
    private Double preco;


    private String imagemUrl;


    private Boolean ativo = true;


    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}