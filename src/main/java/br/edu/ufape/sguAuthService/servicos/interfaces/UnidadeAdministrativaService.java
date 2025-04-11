package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.Gestor;
import br.edu.ufape.sguAuthService.models.Tecnico;

import java.util.List;

public interface UnidadeAdministrativaService {

    UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) throws UnidadeAdministrativaNotFoundException ;

    UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    List<UnidadeAdministrativa> listarUnidadesAdministrativas();

    List<UnidadeAdministrativa> montarArvore();

    List<UnidadeAdministrativa> listarUnidadesFilhas(Long id);

    void deletarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa unidadeAdministrativa, Long id) throws UnidadeAdministrativaNotFoundException;

    void adicionarGestor(Long unidadeId, Long usuarioId);

    void removerGestor(Long unidadeId);

    void adicionarTecnico(Long unidadeId, Long usuarioId);

    void removerTecnico(Long unidadeId, Long usuarioId);

//    void vincularGestor(UnidadeAdministrativa unidade, Gestor gestor);
//
//    void vincularTecnico(Tecnico tecnico, UnidadeAdministrativa unidade);
//
//    void desvincularGestor(UnidadeAdministrativa unidade);
//
//    void desvincularTecnico(Tecnico tecnico);
}
