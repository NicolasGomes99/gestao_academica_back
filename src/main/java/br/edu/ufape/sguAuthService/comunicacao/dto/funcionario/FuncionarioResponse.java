package br.edu.ufape.sguAuthService.comunicacao.dto.funcionario;

import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguAuthService.models.Professor;
import br.edu.ufape.sguAuthService.models.Tecnico;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class FuncionarioResponse extends UsuarioResponse {
    String siape;

    public FuncionarioResponse(Usuario usuario, ModelMapper modelMapper){
        if (usuario == null) throw new IllegalArgumentException("Tecnico não pode ser nulo");
        else modelMapper.map(usuario, this);

        if (usuario.getPerfil(Tecnico.class).isEmpty()) {
            Professor professor = usuario.getPerfil(Professor.class).orElseThrow();
            this.siape = professor.getSiape();
        }else {
            Tecnico tecnico = usuario.getPerfil(Tecnico.class).orElseThrow();
            this.siape = tecnico.getSiape();
        }
    }
}
