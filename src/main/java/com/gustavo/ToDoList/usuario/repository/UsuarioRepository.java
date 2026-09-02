package com.gustavo.ToDoList.usuario.repository;

import com.gustavo.ToDoList.usuario.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    Boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
