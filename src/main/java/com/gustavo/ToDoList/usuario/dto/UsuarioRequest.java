package com.gustavo.ToDoList.usuario.dto;

public record UsuarioRequest(
        String nome,
        String email,
        String senha
){}
