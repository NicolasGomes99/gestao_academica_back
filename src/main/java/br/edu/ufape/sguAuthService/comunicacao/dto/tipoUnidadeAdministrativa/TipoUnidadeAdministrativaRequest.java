package br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TipoUnidadeAdministrativaRequest {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 1, max = 100, message = "O tipo deve ter entre 1 e 100 caracteres")
    private String nome;
    public TipoUnidadeAdministrativa convertToEntity(TipoUnidadeAdministrativaRequest tipoUnidadeAdministrativaRequest, ModelMapper modelMapper) {
        return modelMapper.map(tipoUnidadeAdministrativaRequest, TipoUnidadeAdministrativa.class);
    }
}
