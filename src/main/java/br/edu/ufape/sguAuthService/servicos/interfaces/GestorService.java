package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;

import java.util.List;
import java.util.UUID;

public interface GestorService {
    List<Usuario> listarGestores();

    Usuario buscarGestor(UUID id, boolean isAdm, UUID sessionId) throws GestorNotFoundException, UsuarioNotFoundException;
}
