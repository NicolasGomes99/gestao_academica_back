package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AlunoService {

    //List<Usuario> listarAlunos();

    Page<Usuario> listarAlunos(Pageable pageable);

    Usuario buscarAluno(UUID id, boolean isAdm, UUID sessionId) throws AlunoNotFoundException, UsuarioNotFoundException;

    Usuario buscarAlunoAtual();

}
