package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    Usuario salvar(Usuario usuario);


    Usuario editarUsuario(Usuario novoUsuario) throws UsuarioNotFoundException;

    Usuario buscarUsuario(UUID id, boolean isAdm, UUID sessionId) throws UsuarioNotFoundException;

    Usuario buscarUsuarioAtual() throws UsuarioNotFoundException;

    List<Usuario> listarUsuarios();

    void deletarUsuario(UUID sessionId) throws UsuarioNotFoundException;

    List<Usuario> buscarUsuariosPorIds(List<UUID> ids);
}
