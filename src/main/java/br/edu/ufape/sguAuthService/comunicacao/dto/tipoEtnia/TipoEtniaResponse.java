package br.edu.ufape.sguAuthService.comunicacao.dto.tipoEtnia;

import br.edu.ufape.sguAuthService.models.TipoEtnia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TipoEtniaResponse {
    private Long id;
    private String tipo;

    public  TipoEtniaResponse(TipoEtnia tipoEtnia, ModelMapper modelMapper){
        if (tipoEtnia == null) throw new IllegalArgumentException("Tipo de etnia não pode ser nulo");
        else modelMapper.map(tipoEtnia, this);
    }
}
