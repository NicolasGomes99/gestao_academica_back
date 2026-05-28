package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final Fachada fachada;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotificacoes() {
        UUID userId = authenticatedUserProvider.getUserId();
        return fachada.subscreverNotificacoes(userId);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<NotificacaoEvent>> getHistorico() {
        UUID userId = authenticatedUserProvider.getUserId();
        return ResponseEntity.ok(fachada.buscarNotificacoesNaoLidas(userId));
    }

    @DeleteMapping("/{notificacaoId}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable UUID notificacaoId) {
        UUID userId = authenticatedUserProvider.getUserId();
        fachada.marcarNotificacaoComoLida(userId, notificacaoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/limpar-todas")
    public ResponseEntity<Void> limparTudo() {
        UUID userId = authenticatedUserProvider.getUserId();
        fachada.limparTodasNotificacoes(userId);
        return ResponseEntity.noContent().build();
    }
}