package com.Back_End_Food_Truck.System_Food_Truck.Model;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false, unique = true, length = 11)
    private String telefone;


    @Column(nullable = false)
    private String senha;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;


    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;


    private Boolean ativo = true;
}