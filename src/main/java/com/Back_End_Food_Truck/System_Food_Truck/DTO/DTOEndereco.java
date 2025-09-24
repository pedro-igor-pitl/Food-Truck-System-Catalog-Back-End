package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOEndereco {
    private String rua;
    private String cidade;
    private String cep;
    private String numero;
    private String bairro;
    private String complemento;
}
