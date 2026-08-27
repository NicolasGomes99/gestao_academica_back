package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.notificacao.NotificacaoBroadcastRequest;
import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Page<NotificacaoEvent>> getHistorico(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UUID userId = authenticatedUserProvider.getUserId();
        return ResponseEntity.ok(fachada.buscarNotificacoesNaoLidas(userId, page, size));
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


    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GESTOR')")
    @PostMapping("/broadcast")
    public ResponseEntity<Void> enviarBroadcast(@Valid @RequestBody NotificacaoBroadcastRequest request) {
        fachada.enviarNotificacaoBroadcast(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}