package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
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
public class NotificacaoSseService {

    // Guarda todos os túneis ativos por usuário (CopyOnWrite garante thread-safety)
    private final Map<UUID, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscrever(UUID userId) {
        SseEmitter emitter = new SseEmitter(0L); // 0L = Timeout infinito
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

    public void emitirParaUsuario(UUID userId, NotificacaoEvent evento) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("notificacao").data(evento));
                } catch (IOException e) {
                    removerEmitter(userId, emitter); // Tubo quebrado, removemos
                }
            }
        }
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