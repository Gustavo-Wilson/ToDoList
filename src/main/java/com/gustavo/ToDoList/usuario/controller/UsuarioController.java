package com.gustavo.ToDoList.usuario.controller;

import com.gustavo.ToDoList.usuario.dto.UsuarioLoginRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioResponse;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody UsuarioRequest request){
        if (!service.criar(request)) return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Usuário já cadastrado");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioLoginRequest request){
        Optional<UsuarioResponse> response = service.login(request);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody UsuarioUpdateRequest request){
        if (!service.atualizar(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        return ResponseEntity.ok("Nome alterado com sucesso");
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody UsuarioLoginRequest request){
       if(!service.deletar(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
