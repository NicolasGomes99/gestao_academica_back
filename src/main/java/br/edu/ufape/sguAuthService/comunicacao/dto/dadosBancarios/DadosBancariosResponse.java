package br.edu.ufape.sguAuthService.comunicacao.dto.dadosBancarios;

import br.edu.ufape.sguAuthService.models.DadosBancarios;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DadosBancariosResponse {
    private Long id;
    private String nomeTitular;
    private String conta;
    private String agencia;

    public DadosBancariosResponse(DadosBancarios dadosBancarios, ModelMapper modelMapper) {
        if (dadosBancarios == null) throw new IllegalArgumentException("Dados bancários não podem ser nulos");
        else modelMapper.map(dadosBancarios, this);
    }
}
