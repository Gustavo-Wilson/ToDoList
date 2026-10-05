package com.gustavo.ToDoList.usuario.controller;

import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody UsuarioUpdateRequest request, Authentication authentication){
        service.atualizar(request, authentication);
        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(Authentication authentication){
       service.deletar(authentication);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
