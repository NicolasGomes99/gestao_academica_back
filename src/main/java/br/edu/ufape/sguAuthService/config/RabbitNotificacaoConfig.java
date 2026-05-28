package br.edu.ufape.sguAuthService.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitNotificacaoConfig {

    public static final String QUEUE_SSE = "sgu.notificacoes.auth.sse.queue";
    public static final String EXCHANGE = "sgu.notificacoes.exchange";

    @Bean
    public Queue sseQueue() {
        return new Queue(QUEUE_SSE, true);
    }

    @Bean
    public TopicExchange notificacaoExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingSse(Queue sseQueue, TopicExchange notificacaoExchange) {
        return BindingBuilder.bind(sseQueue).to(notificacaoExchange).with("notificacao.*");
    }
}