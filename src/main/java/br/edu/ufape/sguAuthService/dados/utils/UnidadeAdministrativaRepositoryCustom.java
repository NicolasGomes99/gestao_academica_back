package br.edu.ufape.sguAuthService.dados.utils;

import br.edu.ufape.sguAuthService.models.Funcionario;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UnidadeAdministrativaRepositoryCustom {
    Page<Funcionario> findFuncionariosByUnidadeId(
            Long unidadeId,
            Predicate predicate,
            Pageable pageable
    );
}
