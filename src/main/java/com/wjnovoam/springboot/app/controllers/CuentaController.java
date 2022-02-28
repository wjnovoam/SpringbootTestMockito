package com.wjnovoam.springboot.app.controllers;

import com.wjnovoam.springboot.app.models.Cuenta;
import com.wjnovoam.springboot.app.models.dto.TransaccionDto;
import com.wjnovoam.springboot.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(OK)
    public List<Cuenta> listar(){
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Cuenta detalle(@PathVariable(name = "id") Long id){
        return cuentaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
        cuentaService.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "Ok");
        response.put("mensaje", "Transferencia realizada con exito!");
        response.put("transaccion", dto);

        return ResponseEntity.ok(response);
    }
}
