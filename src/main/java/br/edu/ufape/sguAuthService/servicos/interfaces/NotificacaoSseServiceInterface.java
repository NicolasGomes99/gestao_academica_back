package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificacaoSseServiceInterface {
    SseEmitter subscrever(UUID userId);
    void emitirSinalDeNovaNotificacao(UUID userId);
    void removerTodosEmittersDoUsuario(UUID userId); // Mantemos para o Logout
}