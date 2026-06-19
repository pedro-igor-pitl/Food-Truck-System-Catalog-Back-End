package com.Back_End_Food_Truck.System_Food_Truck.Service;

import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario.A;

@Service
public class ServiceAdmin {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RepositoryAdmin repositoryAdmin;

    @Autowired
    private RepositoryEndereco repositoryEndereco;

    @Autowired
    public ServiceAdmin(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> autenticar(String email, String senha) {

        Optional<Usuario> usuario = repositoryAdmin.findByEmail(email);

        if (usuario.isPresent()
                && passwordEncoder.matches(senha, usuario.get().getSenha())
                && usuario.get().getTipo() == TipoUsuario.A) {

            return usuario;
        }

        return Optional.empty();
    }
}
