package br.edu.ufape.sguAuthService.dados.utils;

import br.edu.ufape.sguAuthService.models.Funcionario;
import br.edu.ufape.sguAuthService.models.QFuncionario;
import br.edu.ufape.sguAuthService.models.QUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class UnidadeAdministrativaRepositoryImpl
        extends QuerydslRepositorySupport
        implements UnidadeAdministrativaRepositoryCustom {

    public UnidadeAdministrativaRepositoryImpl() {
        super(UnidadeAdministrativa.class);
    }

    @Override
    public Page<Funcionario> findFuncionariosByUnidadeId(
            Long unidadeId,
            Predicate predicate,
            Pageable pageable
    ) {
        QUnidadeAdministrativa ua = QUnidadeAdministrativa.unidadeAdministrativa;
        QFuncionario f  = QFuncionario.funcionario;

        JPQLQuery<Funcionario> base = from(ua)
                .join(ua.funcionarios, f)
                .where(ua.id.eq(unidadeId))
                .where(predicate)
                .select(f);

        JPQLQuery<Funcionario> paged = Objects.requireNonNull(getQuerydsl())
                .applyPagination(pageable, base);

        return new PageImpl<>(paged.fetch(),
                pageable,
                base.fetchCount());
    }
}
