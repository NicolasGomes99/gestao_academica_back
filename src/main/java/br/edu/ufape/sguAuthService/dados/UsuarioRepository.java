package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Perfil;
import br.edu.ufape.sguAuthService.models.QUsuario;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, QuerydslPredicateExecutor<Usuario>, QuerydslBinderCustomizer<QUsuario> {
    List<Usuario> findByIdIn(List<UUID> kcIds);

    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.perfis p WHERE TYPE(p) = :perfilClass")
    List<Usuario> findByPerfilType(@Param("perfilClass") Class<? extends Perfil> perfilClass);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QUsuario root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

}