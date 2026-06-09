package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.servicos.interfaces.NotificacaoSseServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class NotificacaoSseService implements NotificacaoSseServiceInterface {

    private final Map<UUID, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscrever(UUID userId) {
        SseEmitter emitter = new SseEmitter(0L);
        userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // Limpa a memória quando a conexão cai ou é fechada pelo frontend
        emitter.onCompletion(() -> removerEmitter(userId, emitter));
        emitter.onTimeout(() -> removerEmitter(userId, emitter));
        emitter.onError((e) -> removerEmitter(userId, emitter));

        // Envia um evento de handshake para forçar a abertura do túnel
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Conectado ao SGU Notifications"));
        } catch (IOException e) {
            removerEmitter(userId, emitter);
        }

        return emitter;
    }

    @Override
    public void emitirSinalDeNovaNotificacao(UUID userId) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("ping").data("nova-notificacao"));
                } catch (IOException e) {
                    removerEmitter(userId, emitter);
                }
            }
        }
    }

    @Override
    public void removerTodosEmittersDoUsuario(UUID userId) {
        userEmitters.remove(userId);
    }

    private void removerEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        }
    }
}