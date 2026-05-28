package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import br.edu.ufape.sguAuthService.servicos.interfaces.NotificacaoRedisServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoRedisService implements NotificacaoRedisServiceInterface {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "notificacoes:nao-lidas:";

    @Override
    public void guardarNotificacaoOffline(UUID userId, NotificacaoEvent evento) {
        String chaveDoUsuario = KEY_PREFIX + userId.toString();
        redisTemplate.opsForHash().put(chaveDoUsuario, evento.id().toString(), evento);
        redisTemplate.expire(chaveDoUsuario, 7, TimeUnit.DAYS);
    }

    @Override
    public List<NotificacaoEvent> buscarNotificacoesNaoLidas(UUID userId) {
        String chaveDoUsuario = KEY_PREFIX + userId.toString();
        List<Object> objetos = redisTemplate.opsForHash().values(chaveDoUsuario);

        return objetos.stream()
                .map(obj -> (NotificacaoEvent) obj)
                .collect(Collectors.toList());
    }

    @Override
    public void marcarUnicaComoLida(UUID userId, UUID notificacaoId) {
        String chaveDoUsuario = KEY_PREFIX + userId.toString();
        redisTemplate.opsForHash().delete(chaveDoUsuario, notificacaoId.toString());
    }

    @Override
    public void marcarTodasComoLidas(UUID userId) {
        String chaveDoUsuario = KEY_PREFIX + userId.toString();
        redisTemplate.delete(chaveDoUsuario);
    }
}