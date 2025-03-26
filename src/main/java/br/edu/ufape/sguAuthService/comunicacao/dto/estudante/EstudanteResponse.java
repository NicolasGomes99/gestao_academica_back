package br.edu.ufape.sguAuthService.comunicacao.dto.estudante;

import br.edu.ufape.sguAuthService.models.DadosBancarios;
import br.edu.ufape.sguAuthService.models.Endereco;
import br.edu.ufape.sguAuthService.models.Estudante;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstudanteResponse {
    private Long id;
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente;
    private String tipoDeficiencia;
    private TipoEtnia tipoEtnia;
    private Endereco endereco;
    private DadosBancarios dadosBancarios;

    public EstudanteResponse(Estudante estudante,  ModelMapper modelMapper) {
        if (estudante == null) throw new IllegalArgumentException("Estudante não pode ser nulo");
        else modelMapper.map(estudante, this);
    }
}
