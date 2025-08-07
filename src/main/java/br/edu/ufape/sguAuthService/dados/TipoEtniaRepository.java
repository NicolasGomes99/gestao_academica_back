package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.QTipoEtnia;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TipoEtniaRepository extends JpaRepository<TipoEtnia, Long>, QuerydslBinderCustomizer<QTipoEtnia>, QuerydslPredicateExecutor<TipoEtnia> {
    Optional<TipoEtnia> findByTipoIgnoreCase(String tipo);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QTipoEtnia root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}

