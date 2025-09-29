package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOListaUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOLogin;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Model.Usuario;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class ControllerAdmin {

    @Autowired
    private ServiceAdmin serviceAdmin;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody DTOLogin DTOLogin) {
        return  serviceAdmin.autenticar(DTOLogin.getEmail(), DTOLogin.getSenha())
                .filter(u -> u.getTipo() == TipoUsuario.A)
                .map(u -> ResponseEntity.status(HttpStatus.NO_CONTENT))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)).body(HttpStatus.UNAUTHORIZED);
    }

}
