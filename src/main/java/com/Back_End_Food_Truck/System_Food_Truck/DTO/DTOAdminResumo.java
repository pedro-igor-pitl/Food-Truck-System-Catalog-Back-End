package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import com.Back_End_Food_Truck.System_Food_Truck.Model.Produto;
import lombok.Data;

@Data
public class DTOAdminResumo {
    private DTOEndereco endereco;
    private DTOCategoria categoria;
    private DTOUsuario usuario;
    private Produto produto;
}
