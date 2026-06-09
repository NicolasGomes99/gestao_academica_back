package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface NotificacaoRedisServiceInterface {

    void guardarNotificacaoOffline(UUID userId, NotificacaoEvent evento);

    Page<NotificacaoEvent> buscarNotificacoesNaoLidas(UUID userId, int page, int size);

    void marcarUnicaComoLida(UUID userId, UUID notificacaoId);

    void marcarTodasComoLidas(UUID userId);


}