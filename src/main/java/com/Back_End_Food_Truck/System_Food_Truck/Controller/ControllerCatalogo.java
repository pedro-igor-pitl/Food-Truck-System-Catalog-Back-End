package com.Back_End_Food_Truck.System_Food_Truck.Controller;

import com.Back_End_Food_Truck.System_Food_Truck.DTO.DTOCatalogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.Back_End_Food_Truck.System_Food_Truck.Service.ServiceCatalogo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class ControllerCatalogo {

    @Autowired
    private ServiceCatalogo serviceCatalogo;

    @GetMapping("/lista")
    public List<DTOCatalogo> listarCatalogo() {return serviceCatalogo.listarCatalogo();}
}

