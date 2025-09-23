package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.Data;

@Data
public class DTOEndereco {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private String bairro;
    private String complemento;
}
