package br.edu.ufape.sguAuthService.comunicacao.dto.estudante;

import br.edu.ufape.sguAuthService.models.Estudante;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstudanteRequest {
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente;
    private String tipoDeficiencia;

    public Estudante convertToEntity(EstudanteRequest estudanteRequest, ModelMapper modelMapper) {
        return modelMapper.map(estudanteRequest, Estudante.class);
    }
}