package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Curso;
import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("SELECT a.usuario FROM Aluno a WHERE a.curso.id = :id")
    List<Usuario> findAllAlunosByCursoId(Long id);
    //List<Curso> findByAtivoTrue();

    Page<Curso> findByAtivoTrue(Pageable pageable);

    boolean existsByNome(String nome);
    Curso findByNomeAndAtivoFalse(String nome);
}