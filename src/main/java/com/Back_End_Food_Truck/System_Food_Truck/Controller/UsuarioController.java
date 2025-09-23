package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOEndereco;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOLogin;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequestMapping("/admin")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DTOLogin DTOLogin) {
        return usuarioService.autenticar(DTOLogin.getEmail(), DTOLogin.getSenha())
                .filter(u -> u.getTipo() == TipoUsuario.A)
                .map(u -> ResponseEntity.ok("Login realizado com sucesso!"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciais inválidas ou não é administrador."));
    }

    @GetMapping("/listaUsuarios")
    public List<DTOListaUsuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody DTOUsuario DTOUsuario) {
        Usuario usuario = usuarioService.cadastrarUsuario(DTOUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }





}
