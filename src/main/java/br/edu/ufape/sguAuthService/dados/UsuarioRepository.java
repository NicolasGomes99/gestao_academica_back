package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    List<Usuario> findByAtivoTrue();
    List<Usuario> findByIdIn(List<UUID> kcIds);

    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Aluno AND u.ativo = true")
    List<Usuario> findUsuariosAlunos();
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Professor AND u.ativo = true")
    List<Usuario> findUsuariosProfessores();
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Tecnico AND u.ativo = true")
    List<Usuario> findUsuariosTecnicos();
    @Query("SELECT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = Gestor AND u.ativo = true")
    List<Usuario> findUsuariosGestores();
}