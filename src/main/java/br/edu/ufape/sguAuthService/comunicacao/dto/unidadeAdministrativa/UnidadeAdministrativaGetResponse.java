package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa.TipoUnidadeAdministrativaResponse;
import br.edu.ufape.sguAuthService.models.Gestor;
import br.edu.ufape.sguAuthService.models.Tecnico;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import org.modelmapper.ModelMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UnidadeAdministrativaGetResponse {
    private Long id;
    private String nome;
    private String codigo;
    private TipoUnidadeAdministrativaResponse tipoUnidadeAdministrativa;
    private Gestor gestor;
    private List<Tecnico> tecnicos;

    public UnidadeAdministrativaGetResponse(UnidadeAdministrativa unidadeAdministrativa, ModelMapper modelMapper) {
        this.id = unidadeAdministrativa.getId();
        this.nome = unidadeAdministrativa.getNome();
        this.codigo = unidadeAdministrativa.getCodigo();
        this.tipoUnidadeAdministrativa = new TipoUnidadeAdministrativaResponse(unidadeAdministrativa.getTipoUnidadeAdministrativa(), modelMapper);
        this.gestor = unidadeAdministrativa.getGestor();
        this.tecnicos = unidadeAdministrativa.getTecnicos();
    }
}

