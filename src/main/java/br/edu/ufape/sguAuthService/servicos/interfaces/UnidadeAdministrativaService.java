package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;

import java.util.List;
import java.util.Set;

public interface UnidadeAdministrativaService {

    UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) throws UnidadeAdministrativaNotFoundException ;

    UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    List<UnidadeAdministrativa> listarUnidadesAdministrativas();

    List<UnidadeAdministrativa> montarArvore();

    List<UnidadeAdministrativa> listarUnidadesFilhas(Long id);

    void deletarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa unidadeAdministrativa, Long id) throws UnidadeAdministrativaNotFoundException;

    void removerGestor(Long unidadeId, Long gestorUnidadeId);

    void adicionarFuncionario(Long unidadeId, Usuario funcionario) throws UnidadeAdministrativaNotFoundException;

    void removerFuncionario(Long unidadeId, Usuario funcionario) throws UnidadeAdministrativaNotFoundException;

    GestorUnidade adicionarGestor(Long unidadeId, GestorUnidade gestorUnidade);


    Set<GestorUnidade> listarGestores(Long unidadeId);

    Set<Funcionario> listarFuncionarios(Long unidadeId);

    List<UnidadeAdministrativa> listarUnidadesPorGestor(Gestor gestor);

    List<UnidadeAdministrativa> listarUnidadesPorFuncionario(Usuario usuario);
}
