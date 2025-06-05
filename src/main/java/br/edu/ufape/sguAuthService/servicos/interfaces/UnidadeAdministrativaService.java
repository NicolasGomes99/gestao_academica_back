package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UnidadeAdministrativaService {

    UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) throws UnidadeAdministrativaNotFoundException ;

    UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    Page<UnidadeAdministrativa> listarUnidadesAdministrativas(Pageable pageable);

    List<UnidadeAdministrativa> montarArvore();

    List<UnidadeAdministrativa> listarUnidadesFilhas(Long id);

    void deletarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa unidadeAdministrativa, Long id) throws UnidadeAdministrativaNotFoundException;

    GestorUnidade adicionarGestor(UnidadeAdministrativa unidade, GestorUnidade gestorUnidade);

    void removerGestor(UnidadeAdministrativa unidade, Long gestorUnidadeId);

    void adicionarFuncionario(UnidadeAdministrativa unidade, Usuario usuario);

    void removerFuncionario(UnidadeAdministrativa unidade, Usuario usuario);

    Set<GestorUnidade> listarGestores(Long unidadeId);

    Set<Funcionario> listarFuncionarios(Long unidadeId);

    List<UnidadeAdministrativa> listarUnidadesPorGestor(Gestor gestor);

    List<UnidadeAdministrativa> listarUnidadesPorFuncionario(Usuario usuario);
}
