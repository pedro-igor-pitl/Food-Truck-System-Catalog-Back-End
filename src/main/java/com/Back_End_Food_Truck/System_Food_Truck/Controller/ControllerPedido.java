package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOPedido;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Pedido;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServicePedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class ControllerPedido {

    @Autowired
    private ServicePedido servicePedido;

    // LISTAR todos os pedidos
    @GetMapping("/lista")
    public List<DTOPedido> listarPedidos() {
        return servicePedido.listarPedidos();
    }

    // BUSCAR pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<DTOPedido> buscarPorId(@PathVariable Long id) {
        return servicePedido.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CRIAR pedido
    @PostMapping("/cadastrar")
    public ResponseEntity<Pedido> cadastrar(@RequestBody DTOPedido dtoPedido) {
        Pedido pedidoCriado = servicePedido.criarPedido(dtoPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCriado);
    }

    // DELETAR pedido
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = servicePedido.deletarPedido(id);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
