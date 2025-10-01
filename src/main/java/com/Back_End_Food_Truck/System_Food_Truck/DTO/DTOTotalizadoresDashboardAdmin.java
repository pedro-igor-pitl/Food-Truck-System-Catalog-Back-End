package com.Back_End_Food_Truck.System_Food_Truck.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOTotalizadoresDashboardAdmin {
    private long totalProdutos;
    private long totalCategorias;
    private long totalClientes;
    private long totalPedidos;
}
