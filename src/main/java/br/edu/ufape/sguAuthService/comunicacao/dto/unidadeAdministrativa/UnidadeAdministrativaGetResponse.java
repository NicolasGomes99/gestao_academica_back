package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import br.edu.ufape.sguAuthService.comunicacao.dto.funcionario.FuncionarioResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.gestorUnidade.GestorUnidadeResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa.TipoUnidadeAdministrativaResponse;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import org.modelmapper.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UnidadeAdministrativaGetResponse {
    private Long id;
    private String nome;
    private String codigo;
    private TipoUnidadeAdministrativaResponse tipoUnidadeAdministrativa;
    private Set<GestorUnidadeResponse> gestores;
    private Set<FuncionarioResponse> funcionarios;

    public UnidadeAdministrativaGetResponse(UnidadeAdministrativa unidade, ModelMapper modelMapper) {
        if (unidade == null)
            throw new IllegalArgumentException("Unidade Administrativa não pode ser nula");

        this.id = unidade.getId();
        this.nome = unidade.getNome();
        this.codigo = unidade.getCodigo();
        this.tipoUnidadeAdministrativa = new TipoUnidadeAdministrativaResponse(unidade.getTipoUnidadeAdministrativa(), modelMapper);

        this.gestores = unidade.getGestores()
                .stream()
                .map(g -> new GestorUnidadeResponse(g, modelMapper))
                .collect(Collectors.toSet());

        this.funcionarios = unidade.getFuncionarios()
                .stream()
                .map(f -> new FuncionarioResponse(f.getUsuario(), modelMapper))
                .collect(Collectors.toSet());
    }
}

