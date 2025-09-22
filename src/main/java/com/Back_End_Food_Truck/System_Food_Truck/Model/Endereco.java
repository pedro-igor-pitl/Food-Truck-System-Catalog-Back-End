package com.Back_End_Food_Truck.System_Food_Truck.Model;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String rua;


    @Column(nullable = false)
    private String numero;


    private String complemento;
    private String bairro;


    @Column(nullable = false)
    private String cidade;


    @Column(nullable = false, length = 8)
    private String cep;
}