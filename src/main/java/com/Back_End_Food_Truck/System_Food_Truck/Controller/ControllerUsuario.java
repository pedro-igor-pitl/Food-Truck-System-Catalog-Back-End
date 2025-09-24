package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class ControllerUsuario {

    @Autowired
    private ServiceUsuario serviceUsuario;

    // Listar todos os usuários
    @GetMapping("/lista")
    public List<DTOListaUsuario> listarUsuarios() {
        return serviceUsuario.listarUsuarios();
    }

    // Buscar por email ou telefone
    @PostMapping("/search")
    public ResponseEntity<DTOListaUsuario> buscarUsuario(@RequestBody Map<String, String> body) {
        String parametro = body.get("parametro");
        return serviceUsuario.buscarPorEmailOuTelefone(parametro)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<DTOListaUsuario> buscarPorId(@PathVariable Long id) {
        return serviceUsuario.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar usuário
    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody DTOUsuario dtoUsuario) {
        Usuario usuario = serviceUsuario.cadastrarUsuario(dtoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    // Atualizar usuário
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody DTOUsuario dtoUsuario) {
        return serviceUsuario.atualizarUsuario(id, dtoUsuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar usuário
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deleted = serviceUsuario.deletarUsuario(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
