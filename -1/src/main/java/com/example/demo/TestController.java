package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // Responde a peticiones GET en http://localhost:8080/api/test
    @GetMapping
    public ResponseEntity<Map<String, String>> testGet() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("mensaje", "Endpoint GET funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    // Responde a peticiones POST enviando un cuerpo JSON
    @PostMapping
    public ResponseEntity<Map<String, Object>> testPost(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("datosRecibidos", body);
        return ResponseEntity.ok(response);
    }
}