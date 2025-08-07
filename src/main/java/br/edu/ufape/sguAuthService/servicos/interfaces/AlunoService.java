package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AlunoService {

    Page<Usuario> listarAlunos(Predicate predicate, Pageable pageable);

    Usuario buscarAluno(UUID id) throws AlunoNotFoundException, UsuarioNotFoundException;

    Usuario buscarAlunoAtual();

}
