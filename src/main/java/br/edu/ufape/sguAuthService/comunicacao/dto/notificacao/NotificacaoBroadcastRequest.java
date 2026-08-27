package br.edu.ufape.sguAuthService.comunicacao.dto.notificacao;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificacaoBroadcastRequest {
    @NotBlank(message = "O perfil destino é obrigatório (ex: TODOS, ALUNO, GESTOR)")
    private String perfilDestino;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A mensagem é obrigatória")
    private String mensagem;

    @NotBlank(message = "O tipo é obrigatório (ex: AVISO_GERAL)")
    private String tipo;
}