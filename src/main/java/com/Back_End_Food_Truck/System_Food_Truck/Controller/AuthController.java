package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.LoginRequest;
import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Repository.RepositoryUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final RepositoryUsuario repositoryUsuario;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        Usuario usuario = repositoryUsuario
                .findByEmail(request.getEmail())
                .orElse(null);

        if(usuario == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Usuário não encontrado");
        }

        if(!usuario.getSenha().equals(request.getSenha())) {
            return ResponseEntity
                    .badRequest()
                    .body("Senha inválida");
        }

        if(usuario.getTipo() != TipoUsuario.A) {
            return ResponseEntity
                    .badRequest()
                    .body("Acesso permitido apenas para administradores");
        }

        if(!usuario.getAtivo()) {
            return ResponseEntity
                    .badRequest()
                    .body("Usuário inativo");
        }

        String token =
                jwtService.gerarToken(
                        usuario.getEmail()
                );

        return ResponseEntity.ok(Map.of(
                "token", token
        ));
    }
}