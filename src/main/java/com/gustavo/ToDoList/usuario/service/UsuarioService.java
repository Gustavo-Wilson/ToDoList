package com.gustavo.ToDoList.usuario.service;

import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.mapper.UsuarioMapper;
import com.gustavo.ToDoList.usuario.model.Usuario;
import com.gustavo.ToDoList.usuario.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public boolean criar(UsuarioRequest request){

      if (repository.existsByEmail(request.email())) return false;

        Usuario usuario = mapper.usuarioRequestToUsuario(request);
        usuario.setSenha(encoder.encode(usuario.getSenha()));

        repository.save(usuario);

        return true;
    }

    public boolean atualizar(UsuarioUpdateRequest request, Authentication authentication){

        String email = authentication.getName();
        Usuario usuario  = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário informado não existe"));

        if (request.novoNome() != null && !request.novoNome().isBlank()){
            usuario.setNome(request.novoNome());
        }

        if (request.novaSenha() != null && !request.novaSenha().isBlank()){
            if (request.senhaAtual() == null || !encoder.matches(request.senhaAtual(), usuario.getSenha())){
                throw new IllegalArgumentException("Senha atual incorreta");
            }
            usuario.setSenha(encoder.encode(request.novaSenha()));
        }

        repository.save(usuario);
        return true;
    }

    //TODO:Criar o logout quando o front for adicionado
    // public boolean sair(){
    //     return true;
    // }

    //TODO:Mudar ao implementar segurança
    public boolean deletar(Authentication authentication){
        String email = authentication.getName();
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário informado não existe"));
        repository.delete(usuario);
        return true;
    }
}
