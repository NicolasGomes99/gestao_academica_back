package br.edu.ufape.sguAuthService.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitNotificacaoConfig {

    public static final String QUEUE_SSE = "sgu.notificacoes.auth.sse.queue";
    public static final String EXCHANGE = "sgu.notificacoes.exchange";

    public static final String DLQ_SSE = "sgu.notificacoes.auth.sse.dlq";
    public static final String DLX_SSE = "sgu.notificacoes.dlx";

    @Bean
    public DirectExchange notificacaoDeadLetterExchange() {
        return new DirectExchange(DLX_SSE);
    }

    @Bean
    public Queue notificacaoDeadLetterQueue() {
        return new Queue(DLQ_SSE, true);
    }

    @Bean
    public Binding notificacaoDeadLetterBinding() {
        return BindingBuilder.bind(notificacaoDeadLetterQueue()).to(notificacaoDeadLetterExchange()).with("notificacao.dlq");
    }

    @Bean
    public Queue sseQueue() {
        return QueueBuilder.durable(QUEUE_SSE)
                .withArgument("x-dead-letter-exchange", DLX_SSE)
                .withArgument("x-dead-letter-routing-key", "notificacao.dlq")
                .build();
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