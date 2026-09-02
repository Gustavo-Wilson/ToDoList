package com.gustavo.ToDoList.usuario.dto;

public record UsuarioLoginRequest(
        String email,
        String senha
) {}
