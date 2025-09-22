package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AuthController {

    @Autowired
    private Usuario usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        return usuarioService.autenticar(email, senha)
                .filter(u -> u.getTipo() == TipoUsuario.A)
                .map(u -> ResponseEntity.ok("Login realizado com sucesso!"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciais inválidas ou não é administrador."));
    }
}
