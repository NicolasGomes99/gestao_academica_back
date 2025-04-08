package br.edu.ufape.sguAuthService.servicos.tasks;

import br.edu.ufape.sguAuthService.config.RabbitConfig;
import br.edu.ufape.sguAuthService.servicos.interfaces.KeycloakServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleAssignmentListener {

    private final KeycloakServiceInterface keycloakService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleRoleAssignment(RoleAssignmentEvent event) {
        try {
            keycloakService.addClientRoleToUser(event.getUserId(), event.getClientId(), event.getRole());
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Erro ao processar evento. Vai para a DLQ", e);
        }
    }
}