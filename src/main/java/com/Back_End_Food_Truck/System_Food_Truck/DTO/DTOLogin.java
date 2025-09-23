package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // construtor vazio
@AllArgsConstructor // construtor com todos os campos
public class DTOLogin {
    private String email;
    private String senha;
}
