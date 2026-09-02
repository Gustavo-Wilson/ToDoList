package com.gustavo.ToDoList.usuario.dto;

public record UsuarioUpdateRequest(
        String email,
        String senha,
        String novoNome
) {}
