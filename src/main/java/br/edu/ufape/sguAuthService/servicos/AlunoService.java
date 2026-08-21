package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.QAluno;
import br.edu.ufape.sguAuthService.models.QUsuario;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service @RequiredArgsConstructor

public class AlunoService implements br.edu.ufape.sguAuthService.servicos.interfaces.AlunoService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Override
    public Page<Usuario> listarAlunos(Predicate predicate, Long cursoId, Pageable pageable) {
        QUsuario qUsuario = QUsuario.usuario;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qUsuario.ativo.isTrue());
        filtroFixo.and(qUsuario.perfis.any().instanceOf(Aluno.class));

        // Se o curso.id foi enviado, fazemos um sub-select seguro garantindo o JOIN
        if (cursoId != null) {
            QAluno qAluno = QAluno.aluno;
            filtroFixo.and(qUsuario.id.in(
                    JPAExpressions.select(qAluno.usuario.id)
                            .from(qAluno)
                            .where(qAluno.curso.id.eq(cursoId))
            ));
        }

        Predicate predicadoFinal = filtroFixo.and(predicate);

        return usuarioRepository.findAll(predicadoFinal, pageable);
    }


    @Override
    public Usuario buscarAluno(UUID id) throws AlunoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);

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
