package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTOListaUsuario {
    private String ativo;
    private String nome;
    private String email;
    private String telefone;
    private String tipo;
    private String rua;
    private String bairro;
    private String cidade;
    private String cep;
    private String numero;
    private String complemento;
}
