package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import br.edu.ufape.sguAuthService.servicos.interfaces.NotificacaoRedisServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    public Page<NotificacaoEvent> buscarNotificacoesNaoLidas(UUID userId, int page, int size) {
        String chaveDoUsuario = KEY_PREFIX + userId.toString();
        List<Object> objetos = redisTemplate.opsForHash().values(chaveDoUsuario);

        // 1. Converte e ordena pela data mais recente primeiro
        List<NotificacaoEvent> todasNotificacoes = objetos.stream()
                .map(obj -> (NotificacaoEvent) obj)
                .sorted(Comparator.comparing(NotificacaoEvent::dataHoraGeracao).reversed())
                .toList();

        // 2. Lógica matemática de paginação (subList)
        int total = todasNotificacoes.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        List<NotificacaoEvent> paginaConteudo = todasNotificacoes.subList(start, end);

        // 3. Retorna o objeto Page padrão do Spring
        return new PageImpl<>(paginaConteudo, PageRequest.of(page, size), total);
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