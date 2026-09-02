package com.gustavo.ToDoList.usuario.mapper;

import com.gustavo.ToDoList.usuario.dto.UsuarioLoginRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioRequest;
import com.gustavo.ToDoList.usuario.dto.UsuarioResponse;
import com.gustavo.ToDoList.usuario.dto.UsuarioUpdateRequest;
import com.gustavo.ToDoList.usuario.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UsuarioMapper {

        @Mapping(target = "id", ignore = true)
        Usuario usuarioRequestToUsuario(UsuarioRequest usuarioRequest);

        UsuarioResponse usuarioToResponse(Usuario usuario);

        UsuarioLoginRequest usuarioUpdateRequestToLogin(UsuarioUpdateRequest usuarioUpdateRequest);

}