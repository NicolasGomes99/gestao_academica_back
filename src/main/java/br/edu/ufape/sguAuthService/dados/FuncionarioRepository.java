package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Funcionario;
import br.edu.ufape.sguAuthService.models.QFuncionario;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>,
        QuerydslPredicateExecutor<Funcionario>, QuerydslBinderCustomizer<QFuncionario> {

  @Override
  default void customize(QuerydslBindings bindings, @NonNull QFuncionario root) {
    bindings.including(
            root.siape,
            root.usuario.nome,
            root.usuario.email,
            root.usuario.telefone,
            root.usuario.cpf
    );
    bindings.bind(String.class)
            .first((StringPath path, String value) ->
                    path.containsIgnoreCase(value));
  }
  }