package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.GestorUnidade;
import br.edu.ufape.sguAuthService.models.QGestorUnidade;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.UUID;


public interface GestorUnidadeRepository extends JpaRepository<GestorUnidade, Long>,
        QuerydslPredicateExecutor<GestorUnidade>, QuerydslBinderCustomizer<QGestorUnidade> {

    boolean existsByUnidadeAdministrativaIdAndGestorUsuarioId(Long unidadeId, UUID usuarioId);


    @Override
    default void customize(QuerydslBindings bindings, @NonNull QGestorUnidade root) {
        bindings.including(
                root.papel,
                root.gestor.usuario.nome,
                root.gestor.usuario.email,
                root.gestor.usuario.telefone,
                root.gestor.usuario.email,
                root.gestor.usuario.cpf,
                root.gestor.siape
        );

        bindings.bind(String.class)
                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}