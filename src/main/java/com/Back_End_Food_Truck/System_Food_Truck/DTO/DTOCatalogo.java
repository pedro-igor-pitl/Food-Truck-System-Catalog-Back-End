package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DTOCatalogo {
    Long id;
    String categoria;
    String produto;
    String descricao;
    Double preco;
    String imagemUrl;
}
