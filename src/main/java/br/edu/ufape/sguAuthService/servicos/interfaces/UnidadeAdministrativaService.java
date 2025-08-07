package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface UnidadeAdministrativaService {

    UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) throws UnidadeAdministrativaNotFoundException ;

    UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    List<UnidadeAdministrativa> listarUnidadesAdministrativas();

    List<UnidadeAdministrativa> montarArvore();

    List<UnidadeAdministrativa> listarUnidadesFilhas(Long id);

    void deletarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa unidadeAdministrativa, Long id) throws UnidadeAdministrativaNotFoundException;

    @Transactional
    GestorUnidade adicionarGestor(UnidadeAdministrativa unidade, GestorUnidade gestorUnidade);

    void removerGestor(UnidadeAdministrativa unidade, Long gestorUnidadeId);

    void adicionarFuncionario(UnidadeAdministrativa unidade, Usuario usuario);

    void removerFuncionario(UnidadeAdministrativa unidade, Usuario usuario);

    Page<GestorUnidade> listarGestores(Long unidadeId, Predicate predicate, Pageable pageable);

    Page<Funcionario> listarFuncionarios(Long unidadeId, Predicate predicate, Pageable pageable);

    Page<UnidadeAdministrativa> listarUnidadesPorGestor(Gestor gestor, Predicate predicate, Pageable pageable);

    Page<UnidadeAdministrativa> listarUnidadesPorFuncionario(Usuario usuario, Predicate predicate, Pageable pageable);
}
