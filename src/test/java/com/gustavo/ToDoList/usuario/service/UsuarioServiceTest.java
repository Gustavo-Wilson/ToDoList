package com.gustavo.ToDoList.usuario.service;

import com.gustavo.ToDoList.usuario.dto.UsuarioLoginRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioResponse;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.mapper.UsuarioMapper;
import com.gustavo.ToDoList.usuario.model.Usuario;
import com.gustavo.ToDoList.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    @Test
    void naoDeveCriarUsuarioSeEmailJaExiste(){

        UsuarioRequest request = new UsuarioRequest("Gustavo", "gustavo@gmail.com","123");
        when(repository.existsByEmail(request.email())).thenReturn(true);

        boolean resultado = service.criar(request);
        assertFalse(resultado);

        verify(repository, never()).save(any(Usuario.class));

    }

    @Test
    void deveCriarUsuario(){

        UsuarioRequest request = new UsuarioRequest("Gustavo", "gustavo@gmail.com","123");
        when(repository.existsByEmail(request.email())).thenReturn(false);

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com","123");

        when(mapper.usuarioRequestToUsuario(request)).thenReturn(usuario);

        boolean resultado = service.criar(request);
        assertTrue(resultado);
        verify(repository).save(usuario);
    }

    @Test
    void naoDeveLogarUsuarioInexistente(){

        UsuarioLoginRequest request = new UsuarioLoginRequest("gustavo@gmail.com", "123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());

        Optional<UsuarioResponse> resultado = service.login(request);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void naoDeveLogarUsuarioComSenhaErrada(){

        UsuarioLoginRequest request = new UsuarioLoginRequest("gustavo@gmail.com", "1234");
        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "123");

        when(repository.findByEmail(request.email())).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponse> resultado = service.login(request);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveLogarUsuario(){

        UsuarioLoginRequest request = new UsuarioLoginRequest("gustavo@gmail.com", "123");
        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(usuario));

        UsuarioResponse response = new UsuarioResponse(null,"Gustavo","gustavo@gmail.com");
        when(mapper.usuarioToResponse(usuario)).thenReturn(response);

        Optional<UsuarioResponse> resultado = service.login(request);
        assertEquals(Optional.of(response), resultado);
    }

    @Test
    void naoDeveDeletarUsuario(){
        UsuarioLoginRequest request = new UsuarioLoginRequest("gustavo@gmail.com","1234");
        Usuario usuario = new Usuario("Gustavo","gustavo@gmail.com","123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(usuario));

        boolean resultado = service.deletar(request);
        assertFalse(resultado);
        verify(repository, never()).delete(any(Usuario.class));
    }

    @Test
    void deveDeletarUsuario(){
        UsuarioLoginRequest request = new UsuarioLoginRequest("gustavo@gmail.com","123");
        Usuario usuario = new Usuario("Gustavo","gustavo@gmail.com","123");
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(usuario));

        boolean resultado = service.deletar(request);
        assertTrue(resultado);
        verify(repository).delete(usuario);
    }

    @Test
    void deveAtualizarNome(){
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("gustavo@gmail.com", "123", "josé");
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("gustavo@gmail.com", "123");
        Usuario usuario = new Usuario("gustavo", "gustavo@gmail.com","123");

        when(mapper.usuarioUpdateRequestToLogin(request)).thenReturn(loginRequest);
        when(repository.findByEmail(loginRequest.email())).thenReturn(Optional.of(usuario));

        boolean resultado = service.atualizar(request);

        assertTrue(resultado);
        assertEquals("josé", usuario.getNome());
        verify(repository).save(usuario);
    }

    @Test
    void naoDeveAtualizarNome(){
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("gustavo@gmail.com", "1234", "josé");
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("gustavo@gmail.com", "1234");
        Usuario usuario = new Usuario("gustavo", "gustavo@gmail.com","123");

        when(mapper.usuarioUpdateRequestToLogin(request)).thenReturn(loginRequest);
        when(repository.findByEmail(loginRequest.email())).thenReturn(Optional.of(usuario));

        boolean resultado = service.atualizar(request);

        assertFalse(resultado);
        verify(repository, never()).save(any(Usuario.class));
    }


}
