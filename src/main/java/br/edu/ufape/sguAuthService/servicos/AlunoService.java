package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor

public class AlunoService implements br.edu.ufape.sguAuthService.servicos.interfaces.AlunoService {
    private final UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(AlunoService.class);
    @Override
    public List<Usuario> listarAlunos() {
        return usuarioRepository.findUsuariosAlunos();
    }

    @Override
    public Usuario buscarAluno(Long id, boolean isAdm, String sessionId) throws AlunoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getKcId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Aluno)) {
            throw new AlunoNotFoundException();
        }
        return usuario;
    }

    @Override
    public Usuario buscarAlunoPorKcId(String kcId) throws UsuarioNotFoundException, AlunoNotFoundException {
        Usuario usuario = usuarioRepository.findByKcId(kcId).orElseThrow(UsuarioNotFoundException::new);
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Aluno)) {
            throw new AlunoNotFoundException();
        }
        return usuario;
    }

    @Override
    public List<Usuario> buscarAlunosPorKcId(List<String> kcIds) {
        List<Usuario> usuarios = usuarioRepository.findByKcIdIn(kcIds);

        Map<String, Usuario> usuarioMap = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getKcId, Function.identity()));

        List<Usuario> users =  kcIds.stream()
                .map(usuarioMap::get)
                .collect(Collectors.toList());

        logger.info("Alunos encontrados: {}", users.getFirst().getAluno());
        return users;
    }

}
