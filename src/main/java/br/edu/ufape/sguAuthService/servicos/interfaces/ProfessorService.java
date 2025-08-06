package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.ProfessorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProfessorService {
    Page<Usuario> listarProfessores(Predicate predicate, Pageable pageable);

    Usuario buscarProfessor(UUID id, boolean isAdm, UUID sessionId) throws ProfessorNotFoundException, UsuarioNotFoundException;

    Usuario buscarProfessorAtual() throws ProfessorNotFoundException, UsuarioNotFoundException;
}
