package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.ProfessorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Professor;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService implements br.edu.ufape.sguAuthService.servicos.interfaces.ProfessorService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Override
    public Page<Usuario> listarProfessores(Pageable pageable) {
        return usuarioRepository.findUsuariosProfessores(pageable);
    }

    @Override
    public Usuario buscarProfessor(UUID id, boolean isAdm, UUID sessionId) throws ProfessorNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Professor)) {
            throw new ProfessorNotFoundException();
        }
        return usuario;
    }

    @Override
    public Usuario buscarProfessorAtual() throws ProfessorNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(authenticatedUserProvider.getUserId()).orElseThrow(UsuarioNotFoundException::new);
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Professor)) {
            throw new ProfessorNotFoundException();
        }
        return usuario;
    }
}
