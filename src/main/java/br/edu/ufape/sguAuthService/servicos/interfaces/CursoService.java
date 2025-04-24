package br.edu.ufape.sguAuthService.servicos.interfaces;


import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.models.Curso;
import br.edu.ufape.sguAuthService.models.Usuario;

import java.util.List;

public interface CursoService {
    Curso salvar(Curso curso);

    Curso buscar(Long id) throws CursoNotFoundException;

    List<Curso> listar();

    List<Usuario> listarAlunosPorCurso(Long id);

    Curso editar(Long id, Curso novoCurso) throws CursoNotFoundException;

    void deletar(Long id) throws CursoNotFoundException;
}
