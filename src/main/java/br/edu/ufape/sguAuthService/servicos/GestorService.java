package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.Gestor;
import br.edu.ufape.sguAuthService.models.QUsuario;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service @RequiredArgsConstructor
public class GestorService implements br.edu.ufape.sguAuthService.servicos.interfaces.GestorService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<Usuario> listarGestores(Predicate predicate, Pageable pageable) {
        QUsuario qUsuario = QUsuario.usuario;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qUsuario.ativo.isTrue());
        filtroFixo.and(qUsuario.perfis.any().instanceOf(Gestor.class));

        Predicate predicadoFinal = filtroFixo.and(predicate);

        return usuarioRepository.findAll(predicadoFinal, pageable);
    }

    @Override
    public Usuario buscarGestor(UUID id, boolean isAdm, UUID sessionId) throws GestorNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Gestor)) {
            throw new GestorNotFoundException();
        }
        return usuario;
    }
}
