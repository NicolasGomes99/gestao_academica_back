package br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeAdministrativaAlocarUsuarioRequest {
    @NotNull(message = "O id da unidade é obrigatório")
    private Long unidadeAdministrativaId;

    @NotNull(message = "O id do usuário é obrigatório")
    private UUID usuarioId;
}
