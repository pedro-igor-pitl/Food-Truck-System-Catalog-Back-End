package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import lombok.Data;

@Data
public class DTOUsuario {
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private String telefone;
    private DTOEndereco endereco;

}
