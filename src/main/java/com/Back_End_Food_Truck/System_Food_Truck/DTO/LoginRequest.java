package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String senha;

}