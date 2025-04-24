package br.edu.ufape.sguAuthService.comunicacao.dto.tecnico;


import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguAuthService.models.Tecnico;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
public class TecnicoResponse extends UsuarioResponse {
    String siape;

    public TecnicoResponse(Usuario usuario, ModelMapper modelMapper){
        if (usuario == null) throw new IllegalArgumentException("Tecnico não pode ser nulo");
        else modelMapper.map(usuario, this);
        Tecnico tecnico = usuario.getPerfil(Tecnico.class).orElseThrow();
        this.siape = tecnico.getSiape();
    }
}
