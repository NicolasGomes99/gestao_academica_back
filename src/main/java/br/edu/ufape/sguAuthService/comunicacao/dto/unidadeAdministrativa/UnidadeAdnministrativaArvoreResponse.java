package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa.TipoUnidadeAdministrativaResponse;

import java.util.List;

public record UnidadeAdnministrativaArvoreResponse(
        Long id,
        String nome,
        String codigo,
        TipoUnidadeAdministrativaResponse tipoUnidadeAdministrativa,
        List<UnidadeAdnministrativaArvoreResponse> filhos
) {}
