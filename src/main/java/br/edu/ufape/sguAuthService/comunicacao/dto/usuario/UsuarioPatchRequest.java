package br.edu.ufape.sguAuthService.comunicacao.dto.usuario;

import br.edu.ufape.sguAuthService.comunicacao.annotations.NumeroValido;
import br.edu.ufape.sguAuthService.models.Usuario;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPatchRequest {

    @Size(min = 1, max = 100, message = "O curso deve ter entre 1 e 100 caracteres")
    private String nome;

    @Size(min = 1, max = 100, message = "O curso deve ter entre 1 e 100 caracteres")
    private String nomeSocial;

    @NumeroValido
    private String telefone;

    public Usuario convertToEntity(UsuarioPatchRequest usuarioRequest, ModelMapper modelMapper) {
        return modelMapper.map(usuarioRequest, Usuario.class);
    }
}
