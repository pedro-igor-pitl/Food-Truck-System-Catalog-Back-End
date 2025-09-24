package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCategoria;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Categoria;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class ControllerCategoria {

    @Autowired
    private ServiceCategoria serviceCategoria;

    // LISTAR todas as categorias
    @GetMapping("/lista")
    public List<DTOCategoria> listarCategorias() {
        return serviceCategoria.listarCategorias();
    }

    // BUSCAR por ID
    @GetMapping("/{id}")
    public ResponseEntity<DTOCategoria> buscarPorId(@PathVariable Long id) {
        return serviceCategoria.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CRIAR categoria
    @PostMapping("/cadastrar")
    public ResponseEntity<Categoria> cadastrar(@RequestBody DTOCategoria dtoCategoria) {
        Categoria categoria = serviceCategoria.criarCategoria(dtoCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    // ATUALIZAR categoria
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @RequestBody DTOCategoria dtoCategoria) {
        return serviceCategoria.atualizarCategoria(id, dtoCategoria)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETAR categoria
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deleted = serviceCategoria.deletarCategoria(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
