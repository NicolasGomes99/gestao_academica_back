package br.edu.ufape.sguAuthService.comunicacao.mensageria;

import br.edu.ufape.sguAuthService.servicos.NotificacaoRedisService;
import br.edu.ufape.sguAuthService.servicos.KeycloakService;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.models.Usuario;
import br.edu.ufape.sguAuthService.models.Perfil;
import br.edu.ufape.sguAuthService.servicos.interfaces.NotificacaoSseServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoSseServiceInterface sseService;
    private final NotificacaoRedisService redisService;
    private final UsuarioRepository usuarioRepository;
    private final KeycloakService keycloakService;

    @RabbitListener(queues = "sgu.notificacoes.auth.sse.queue")
    public void receberNotificacao(NotificacaoEvent evento) {
        if (evento.destinatarioId() != null) {
            processarNotificacao(evento.destinatarioId(), evento);
        } else if (evento.perfilDestino() != null) {
            String destino = evento.perfilDestino().toUpperCase();
            if (destino.equals("ADMINISTRADOR")) {
                distribuirParaRoleKeycloak(destino, evento);
            } else {
                distribuirParaPerfilBanco(destino, evento);
            }
        }
    }

    private void processarNotificacao(UUID usuarioId, NotificacaoEvent evento) {
        redisService.guardarNotificacaoOffline(usuarioId, evento);
        sseService.emitirSinalDeNovaNotificacao(usuarioId);
    }

    private void distribuirParaRoleKeycloak(String roleName, NotificacaoEvent evento) {
        List<UUID> admins = keycloakService.obterUsuariosPorRole(roleName);
        for (UUID adminId : admins) {
            processarNotificacao(adminId, evento);
        }
    }

    private void distribuirParaPerfilBanco(String nomePerfil, NotificacaoEvent evento) {
        Class<? extends Perfil> classePerfil = mapearNomeParaClasse(nomePerfil);
        if (classePerfil != null) {
            List<Usuario> destinatarios = usuarioRepository.findByPerfilType(classePerfil);
            for (Usuario usuario : destinatarios) {
                processarNotificacao(usuario.getId(), evento);
            }
        } else {
            log.warn("Classe de entidade de perfil não encontrada para a string: {}", nomePerfil);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Perfil> mapearNomeParaClasse(String nomePerfil) {
        try {
            String nomeFormatado = nomePerfil.substring(0, 1).toUpperCase() + nomePerfil.substring(1).toLowerCase();
            String fullClassName = "br.edu.ufape.sguAuthService.models." + nomeFormatado;
            Class<?> clazz = Class.forName(fullClassName);

            if (Perfil.class.isAssignableFrom(clazz)) {
                return (Class<? extends Perfil>) clazz;
            }
        } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {
            log.warn("Falha ao mapear perfil destino '{}'.", nomePerfil);
        }
        return null;
    }
}