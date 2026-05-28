package br.edu.ufape.sguAuthService.comunicacao.mensageria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publicar(NotificacaoEvent evento) {
        // Envia para a mesma Exchange que o PRAE Service envia
        rabbitTemplate.convertAndSend("sgu.notificacoes.exchange", "notificacao.auth", evento);
        log.info("Evento de notificação publicado: [{}] {}", evento.tipo(), evento.titulo());
    }
}