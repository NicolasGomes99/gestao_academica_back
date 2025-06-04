package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Page<Usuario> findByAtivoTrue(Pageable pageable);
    List<Usuario> findByIdIn(List<UUID> kcIds);

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Aluno AND u.ativo = true")
    Page<Usuario> findUsuariosAlunos(Pageable pageable);
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Professor AND u.ativo = true")
    Page<Usuario> findUsuariosProfessores(Pageable pageable);
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Tecnico AND u.ativo = true")
    Page<Usuario> findUsuariosTecnicos(Pageable pageable);
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Gestor AND u.ativo = true")
    Page<Usuario> findUsuariosGestores(Pageable pageable);
}