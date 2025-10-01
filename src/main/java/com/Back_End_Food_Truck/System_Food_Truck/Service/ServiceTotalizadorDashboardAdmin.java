package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOTotalizadoresDashboardAdmin;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryPedido;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryProduto;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTotalizadorDashboardAdmin {

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private RepositoryCategoria repositoryCategoria;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private RepositoryPedido repositoryPedido;

    public DTOTotalizadoresDashboardAdmin getTotalizadores() {
        return new DTOTotalizadoresDashboardAdmin(
                repositoryProduto.count(),
                repositoryCategoria.count(),
                repositoryUsuario.count(),
                repositoryPedido.count()
        );
    }
}
