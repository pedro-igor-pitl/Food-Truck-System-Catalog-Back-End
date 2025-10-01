package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOLogin;
import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOTotalizadoresDashboardAdmin;
import com.Back_End_Food_Truck.System_Food_Truck.Model.TipoUsuario;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceAdmin;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceTotalizadorDashboardAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class ControllerAdmin {

    private final ServiceAdmin serviceAdmin;
    private final ServiceTotalizadorDashboardAdmin serviceTotalizadorDashboardAdmin;

    public ControllerAdmin(ServiceAdmin serviceAdmin,
                           ServiceTotalizadorDashboardAdmin serviceTotalizadorDashboardAdmin) {
        this.serviceAdmin = serviceAdmin;
        this.serviceTotalizadorDashboardAdmin = serviceTotalizadorDashboardAdmin;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody DTOLogin dtoLogin) {
        return serviceAdmin.autenticar(dtoLogin.getEmail(), dtoLogin.getSenha())
                .filter(u -> u.getTipo() == TipoUsuario.A)
                .map(u -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/totalizadores")
    public DTOTotalizadoresDashboardAdmin getTotalizadores() {
        return serviceTotalizadorDashboardAdmin.getTotalizadores();
    }
}
