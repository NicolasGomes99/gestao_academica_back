package br.edu.ufape.sguAuthService.comunicacao.dto.estudante;

import br.edu.ufape.sguAuthService.models.Estudante;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstudanteResponse {
    private Long id;
    private BigDecimal rendaPercapta;
    private String contatoFamilia;
    private boolean deficiente;
    private String tipoDeficiencia;

    public EstudanteResponse(Estudante estudante) {
        this.id = estudante.getId();
        this.rendaPercapta = estudante.getRendaPercapta();
        this.contatoFamilia = estudante.getContatoFamilia();
        this.deficiente = estudante.isDeficiente();
        this.tipoDeficiencia = estudante.getTipoDeficiencia();
    }
}
