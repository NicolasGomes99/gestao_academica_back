package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor

public class AlunoService implements br.edu.ufape.sguAuthService.servicos.interfaces.AlunoService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

//    @Override
//    public List<Usuario> listarAlunos() {
//        return usuarioRepository.findUsuariosAlunos();
//    }

    @Override
    public Page<Usuario> listarAlunos(Pageable pageable) {
        return usuarioRepository.findUsuariosAlunos(pageable);
    }


    @Override
    public Usuario buscarAluno(UUID id, boolean isAdm, UUID sessionId) throws AlunoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Aluno)) {
            throw new AlunoNotFoundException();
        }
        return usuario;
    }

    @Override
    public Usuario buscarAlunoAtual() {
        Usuario usuario = usuarioRepository.findById(authenticatedUserProvider.getUserId()).orElseThrow(UsuarioNotFoundException::new);
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Aluno)) {
            throw new AlunoNotFoundException();
        }
        return usuario;
    }




}
