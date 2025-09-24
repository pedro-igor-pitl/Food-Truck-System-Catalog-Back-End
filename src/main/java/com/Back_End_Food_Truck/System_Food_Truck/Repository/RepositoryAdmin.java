package com.Back_End_Food_Truck.System_Food_Truck.Repository;

import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryAdmin extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndSenha(String email, String senha); //Login
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefone(String telefone);
}
