package com.gustavo.ToDoList.usuario.dto;

public record UsuarioUpdateRequest(
        String senhaAtual,
        String novaSenha,
        String novoNome
) {}
