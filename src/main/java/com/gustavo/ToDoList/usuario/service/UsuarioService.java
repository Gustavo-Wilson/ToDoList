package com.gustavo.ToDoList.usuario.service;

import com.gustavo.ToDoList.usuario.dto.UsuarioLoginRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioResponse;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.mapper.UsuarioMapper;
import com.gustavo.ToDoList.usuario.model.Usuario;
import com.gustavo.ToDoList.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public boolean criar(UsuarioRequest request){

      if (repository.existsByEmail(request.email())) return false;

        // TODO: Adicionar hash na senha

        Usuario usuario = mapper.usuarioRequestToUsuario(request);

        repository.save(usuario);

        return true;
    }

    //TODO: Adicionar token jwt e spring security
    public Optional<UsuarioResponse> login(UsuarioLoginRequest request){
        Optional<Usuario> usuarioOptional = autenticar(request);
        if (usuarioOptional.isEmpty()) return Optional.empty();

        Usuario usuario = usuarioOptional.get();

        UsuarioResponse response = mapper.usuarioToResponse(usuario);
        return Optional.of(response);
    }

    public boolean atualizar(UsuarioUpdateRequest request){

        Optional<Usuario> usuarioOptional = autenticar(mapper.usuarioUpdateRequestToLogin(request));
        if (usuarioOptional.isEmpty()) return false;

        Usuario usuario = usuarioOptional.get();

        usuario.setNome(request.novoNome());
        repository.save(usuario);
        return true;
    }

    //TODO:Implementar logout após adicionar JWT e spring security
    public boolean sair(){
        return true;
    }

    //TODO:Mudar ao implementar segurança
    public boolean deletar(UsuarioLoginRequest request){
        Optional<Usuario> usuarioOptional = autenticar(request);
        if (usuarioOptional.isEmpty()) return false;

        Usuario usuario = usuarioOptional.get();

        repository.delete(usuario);
        return true;
    }

    private Optional<Usuario> autenticar(UsuarioLoginRequest request){

        Optional<Usuario> usuarioOptional = repository.findByEmail(request.email());
        if(usuarioOptional.isEmpty()) return Optional.empty();

        Usuario usuario = usuarioOptional.get();

        if(!usuario.getSenha().equals(request.senha())) return Optional.empty();
        return Optional.of(usuario);
    }
}
