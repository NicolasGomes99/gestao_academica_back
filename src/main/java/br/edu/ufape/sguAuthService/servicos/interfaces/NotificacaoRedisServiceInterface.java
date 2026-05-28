package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import java.util.List;
import java.util.UUID;

public interface NotificacaoRedisServiceInterface {

    void guardarNotificacaoOffline(UUID userId, NotificacaoEvent evento);

    List<NotificacaoEvent> buscarNotificacoesNaoLidas(UUID userId);

    void marcarUnicaComoLida(UUID userId, UUID notificacaoId);

    void marcarTodasComoLidas(UUID userId);


}