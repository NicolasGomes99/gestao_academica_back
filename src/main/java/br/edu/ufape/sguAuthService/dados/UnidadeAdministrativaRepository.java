package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.dados.utils.UnidadeAdministrativaRepositoryCustom;
import br.edu.ufape.sguAuthService.models.QUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.List;

public interface  UnidadeAdministrativaRepository extends JpaRepository<UnidadeAdministrativa, Long>,
        QuerydslPredicateExecutor<UnidadeAdministrativa>, QuerydslBinderCustomizer<QUnidadeAdministrativa>, UnidadeAdministrativaRepositoryCustom {
    List<UnidadeAdministrativa> findByUnidadePaiId(Long id);
    List<UnidadeAdministrativa> findByUnidadePaiIsNull();

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QUnidadeAdministrativa root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

}
