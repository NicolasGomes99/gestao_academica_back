package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.ProfessorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;

import java.util.List;
import java.util.UUID;

public interface ProfessorService {
    List<Usuario> listarProfessores();

    Usuario buscarProfessor(UUID id, boolean isAdm, UUID sessionId) throws ProfessorNotFoundException, UsuarioNotFoundException;

    Usuario buscarProfessorAtual() throws ProfessorNotFoundException, UsuarioNotFoundException;
}
