package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario.A;

@Service
public class ServiceAdmin {

    @Autowired
    private RepositoryAdmin repositoryAdmin;

    @Autowired
    private RepositoryEndereco repositoryEndereco;

    public Optional<Usuario> autenticar(String email, String senha) {
        return repositoryAdmin.findByEmailAndSenha(email, senha)
                .filter(u -> u.getTipo() == A);
    }
}
