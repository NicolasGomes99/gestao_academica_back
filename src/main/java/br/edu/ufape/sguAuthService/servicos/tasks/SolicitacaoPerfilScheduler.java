package br.edu.ufape.sguAuthService.servicos.tasks;

import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoEvent;
import br.edu.ufape.sguAuthService.comunicacao.mensageria.NotificacaoPublisher;
import br.edu.ufape.sguAuthService.dados.SolicitacaoPerfilRepository;
import br.edu.ufape.sguAuthService.models.Enums.StatusSolicitacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitacaoPerfilScheduler {

    private final SolicitacaoPerfilRepository solicitacaoPerfilRepository;
    private final NotificacaoPublisher notificacaoPublisher;

    /**
     * Roda de segunda a sexta, às 08:00, 14:00 e 17:00.
     * Expressão Cron: Seg(0) Min(0) Hora(8,14,17) DiaMes(*) Mes(*) DiaSem(MON-FRI)
     */
    @Scheduled(cron = "0 0 8,14,17 * * MON-FRI")
    public void notificarResumoSolicitacoesPendentes() {
        log.info("[ROTINA] Contando solicitações de perfil pendentes...");

        long totalPendentes = solicitacaoPerfilRepository.countByStatus(StatusSolicitacao.PENDENTE);

        if (totalPendentes > 0) {
            String msg = String.format("Existem %d solicitações de perfil aguardando a sua avaliação neste momento.", totalPendentes);

            // Dispara a notificação de resumo para todos os usuários com o perfil de ADMINISTRADOR
            notificacaoPublisher.publicar(NotificacaoEvent.paraPerfil(
                    "ADMINISTRADOR",
                    "Resumo de Solicitações Pendentes",
                    msg,
                    "RESUMO_SOLICITACOES"
            ));

            log.info("[ROTINA] Notificação de resumo enviada aos administradores. Total: {}", totalPendentes);
        }
    }
}