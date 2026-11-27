package cl.duoc.kembang.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.kembang.model.Comuna;
import cl.duoc.kembang.service.ComunaService;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    private final ComunaService comunaService;

    public ComunaController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    @GetMapping
    public ResponseEntity<List<Comuna>> listar() {
        return ResponseEntity.ok(comunaService.listar());
    }
}