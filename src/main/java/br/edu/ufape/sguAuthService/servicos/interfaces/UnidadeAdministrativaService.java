package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;

import java.util.List;

public interface UnidadeAdministrativaService {

    UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) throws UnidadeAdministrativaNotFoundException ;

    UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    List<UnidadeAdministrativa> listarUnidadesAdministrativas();

    List<UnidadeAdministrativa> montarArvore();

    List<UnidadeAdministrativa> listarUnidadesFilhas(Long id);

    void deletarUnidadeAdministrativa(Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa unidadeAdministrativa, Long id) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa adicionarGestor(Long unidadeId, Usuario gestor) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa adicionarTecnico(Long unidadeId, Usuario tecnico) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa removerGestor(Long unidadeId) throws UnidadeAdministrativaNotFoundException;

    UnidadeAdministrativa removerTecnico(Long unidadeId, Usuario tecnico) throws UnidadeAdministrativaNotFoundException;

}
