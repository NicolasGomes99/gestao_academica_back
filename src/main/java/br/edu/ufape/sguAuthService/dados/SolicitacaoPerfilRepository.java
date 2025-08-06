package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Enums.StatusSolicitacao;
import br.edu.ufape.sguAuthService.models.QSolicitacaoPerfil;
import br.edu.ufape.sguAuthService.models.SolicitacaoPerfil;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SolicitacaoPerfilRepository extends JpaRepository<SolicitacaoPerfil, Long>, QuerydslPredicateExecutor<SolicitacaoPerfil>, QuerydslBinderCustomizer<QSolicitacaoPerfil> {
    List<SolicitacaoPerfil> findBySolicitanteAndStatusIn(Usuario solicitante, Collection<StatusSolicitacao> status);

    Page<SolicitacaoPerfil> findAllBySolicitante_Id(UUID id, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, @NonNull QSolicitacaoPerfil root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

}