package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UnidadeAdministrativaPatchRequest {
    @Size(min = 1, max = 100, message = "O nome deve ter entre 1 e 100 caracteres")
    private String nome;
    @Size(min = 1, max = 100, message = "O Código deve ter entre 1 e 100 caracteres")
    private String codigo;


    public UnidadeAdministrativaPatchRequest convertToEntity(UnidadeAdministrativaPatchRequest unidadeAdministrativaPatchRequest, ModelMapper modelMapper) {
        return modelMapper.map(unidadeAdministrativaPatchRequest, UnidadeAdministrativaPatchRequest.class);
    }
}
