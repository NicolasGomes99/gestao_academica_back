package br.edu.ufape.sguAuthService.servicos.interfaces;


import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.models.Curso;
import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CursoService {
    Curso salvar(Curso curso);

    Curso buscar(Long id) throws CursoNotFoundException;

    Page<Curso> listar(Pageable pageable);

    List<Usuario> listarAlunosPorCurso(Long id);

    Curso editar(Long id, Curso novoCurso) throws CursoNotFoundException;

    void deletar(Long id) throws CursoNotFoundException;
}
