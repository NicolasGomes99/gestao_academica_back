package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeAdministrativaResponse {
    private Long id;
    private String nome;
    private String codigo;
    private TipoUnidadeAdministrativa tipoUnidadeAdministrativa;
//    private List<UnidadeAdministrativaResponse> unidadesFilhas = new ArrayList<>();




    public UnidadeAdministrativaResponse(UnidadeAdministrativa unidadeAdministrativa, ModelMapper modelMapper) {
        if (unidadeAdministrativa == null) throw new IllegalArgumentException("Unidade Administrativa não pode ser nula");
        modelMapper.map(unidadeAdministrativa, this);

    }
}
