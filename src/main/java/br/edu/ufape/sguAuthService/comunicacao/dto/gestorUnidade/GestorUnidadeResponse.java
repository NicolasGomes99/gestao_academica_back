package br.edu.ufape.sguAuthService.comunicacao.dto.gestorUnidade;

import br.edu.ufape.sguAuthService.comunicacao.dto.gestor.GestorResponse;
import br.edu.ufape.sguAuthService.models.GestorUnidade;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class GestorUnidadeResponse {
    private Long id;
    private String papel;
    private GestorResponse gestor;

    public GestorUnidadeResponse(GestorUnidade gestorUnidade, ModelMapper modelMapper) {
        if (gestorUnidade == null)
            throw new IllegalArgumentException("GestorUnidade não pode ser nulo");

        this.id = gestorUnidade.getId();
        this.papel = gestorUnidade.getPapel();

        Usuario usuarioGestor = gestorUnidade.getGestor().getUsuario();
        this.gestor = new GestorResponse(usuarioGestor, modelMapper);
    }
}