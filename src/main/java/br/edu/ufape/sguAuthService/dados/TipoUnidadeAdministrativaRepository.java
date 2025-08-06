package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.QTipoUnidadeAdministrativa;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

public interface  TipoUnidadeAdministrativaRepository extends JpaRepository<TipoUnidadeAdministrativa, Long>,
        QuerydslPredicateExecutor<TipoUnidadeAdministrativa>, QuerydslBinderCustomizer<QTipoUnidadeAdministrativa> {
   TipoUnidadeAdministrativa findByNome(String nome);

   @Override
   default void customize(QuerydslBindings bindings, @NonNull QTipoUnidadeAdministrativa root) {
      bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
   }

}
