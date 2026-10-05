package com.gustavo.ToDoList.usuario.service;

import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.mapper.UsuarioMapper;
import com.gustavo.ToDoList.usuario.model.Usuario;
import com.gustavo.ToDoList.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private Authentication authentication;

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
        when(encoder.encode(usuario.getSenha())).thenReturn("SenhaCriptografada");

        boolean resultado = service.criar(request);

        assertTrue(resultado);
        assertEquals("SenhaCriptografada", usuario.getSenha());

        verify(encoder).encode("123");
        verify(repository).save(usuario);
    }

    @Test
    void deveAtualizarNome(){
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, null, "José");

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senhaCriptografada");
        when(authentication.getName()).thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        service.atualizar(request, authentication);

        assertEquals("José", usuario.getNome());
        verify(repository).save(usuario);

    }

    @Test
    void deveAtualizarSenha(){
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("123", "999", null);
        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senhaAntiga");

        when(authentication.getName()).thenReturn(usuario.getEmail());
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(true);
        when(encoder.encode(request.novaSenha())).thenReturn("senhaNovaCriptografada");

        service.atualizar(request, authentication);
        assertEquals("senhaNovaCriptografada", usuario.getSenha());

        verify(encoder).matches("123", "senhaAntiga");
        verify(encoder).encode("999");
        verify(repository).save(usuario);
    }

    @Test
    void naoDeveAtualizarSenhaSeSenhaAtualEstiverErrada() {

        UsuarioUpdateRequest request = new UsuarioUpdateRequest("senhaErrada", "456", null);

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senhaAntiga");

        when(authentication.getName()).thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        when(encoder.matches(request.senhaAtual(), usuario.getSenha()))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.atualizar(request, authentication)
        );

        verify(repository, never()).save(any(Usuario.class));
        verify(encoder, never()).encode("456");
    }

    @Test
    void naoDeveAtualizarSenhaSeSenhaAtualNaoForInformada() {

        UsuarioUpdateRequest request = new UsuarioUpdateRequest(null, "456", null);

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senhaAntiga");

        when(authentication.getName()).thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.atualizar(request, authentication)
        );

        verify(repository, never()).save(any(Usuario.class));
        verify(encoder, never()).encode("456");
    }

    @Test
    void deveAtualizarNomeESenha() {

        UsuarioUpdateRequest request = new UsuarioUpdateRequest( "123", "456", "José");

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senhaAntiga");

        when(authentication.getName()).thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        when(encoder.matches(request.senhaAtual(), usuario.getSenha())).thenReturn(true);

        when(encoder.encode(request.novaSenha())).thenReturn("senhaNovaCriptografada");

        service.atualizar(request, authentication);

        assertEquals("José", usuario.getNome());
        assertEquals("senhaNovaCriptografada", usuario.getSenha());

        verify(repository).save(usuario);
    }


    @Test
    void deveDeletarUsuario() {

        Usuario usuario = new Usuario("Gustavo", "gustavo@gmail.com", "senha");

        when(authentication.getName()).thenReturn(usuario.getEmail());

        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        boolean resultado = service.deletar(authentication);

        assertTrue(resultado);

        verify(repository).delete(usuario);
    }

    @Test
    void naoDeveDeletarUsuarioInexistente() {

        when(authentication.getName()).thenReturn("gustavo@gmail.com");

        when(repository.findByEmail("gustavo@gmail.com")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.deletar(authentication)
        );

        verify(repository, never()).delete(any(Usuario.class));
    }
}
