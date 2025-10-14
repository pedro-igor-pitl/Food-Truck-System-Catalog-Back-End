package com.Back_End_Food_Truck.System_Food_Truck.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Data
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;


    private String descricao;


    private Boolean ativo = true;


    @JsonIgnore
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Produto> produtos;
}

