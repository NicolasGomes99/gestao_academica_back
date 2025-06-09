package br.edu.ufape.sguAuthService.comunicacao.dto.solicitacaoPerfil;

import br.edu.ufape.sguAuthService.models.SolicitacaoPerfil;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class SolicitacaoPerfilRequest {

    @Size(max = 255, message = "O parecer deve ter no máximo 255 caracteres")
    private String parecer;

    public SolicitacaoPerfil convertToEntity(ModelMapper modelMapper) {
        return modelMapper.map(this, SolicitacaoPerfil.class);
    }
}
