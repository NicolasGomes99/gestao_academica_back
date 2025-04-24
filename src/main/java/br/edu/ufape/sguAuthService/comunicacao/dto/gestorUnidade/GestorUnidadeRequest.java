package br.edu.ufape.sguAuthService.comunicacao.dto.gestorUnidade;

import br.edu.ufape.sguAuthService.models.GestorUnidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor
public class GestorUnidadeRequest {
    private UUID usuarioId;
    private String papel;

    public GestorUnidade convertToEntity(GestorUnidadeRequest gestorUnidadeRequest, ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        if (gestorUnidadeRequest == null) throw new IllegalArgumentException("GestorUnidadeRequest não pode ser nulo");
        else return modelMapper.map(gestorUnidadeRequest, GestorUnidade.class);
    }
}
