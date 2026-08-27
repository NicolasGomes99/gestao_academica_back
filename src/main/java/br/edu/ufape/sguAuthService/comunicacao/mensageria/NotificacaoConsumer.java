package br.edu.ufape.sguAuthService.comunicacao.mensageria;

import br.edu.ufape.sguAuthService.servicos.NotificacaoRedisService;
import br.edu.ufape.sguAuthService.servicos.KeycloakService;
import br.edu.ufape.sguAuthService.servicos.EmailService;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.models.Usuario;
import br.edu.ufape.sguAuthService.models.Perfil;
import br.edu.ufape.sguAuthService.servicos.interfaces.NotificacaoSseServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private final EmailService emailService; // Novo serviço de e-mail injetado

    @RabbitListener(queues = "sgu.notificacoes.auth.sse.queue")
    public void receberNotificacao(NotificacaoEvent evento) {
        List<String> emailsParaDisparo = new ArrayList<>();

        // 1. Notificação individual
        if (evento.destinatarioId() != null) {
            Usuario usuario = usuarioRepository.findById(evento.destinatarioId()).orElse(null);
            if (usuario != null && Boolean.TRUE.equals(usuario.getAtivo())) {
                processarNotificacao(usuario.getId(), evento);
                if (usuario.getEmail() != null) emailsParaDisparo.add(usuario.getEmail());
            }
        }
        // 2. Notificação em Broadcast (Grupos ou Todos)
        else if (evento.perfilDestino() != null) {
            String destino = evento.perfilDestino().toUpperCase();

            if (destino.equals("TODOS")) {
                emailsParaDisparo.addAll(distribuirParaTodos(evento));
            } else if (destino.equals("ADMINISTRADOR")) {
                emailsParaDisparo.addAll(distribuirParaRoleKeycloak(destino, evento));
            } else {
                emailsParaDisparo.addAll(distribuirParaPerfilBanco(destino, evento));
            }
        }

        // 3. Disparo de E-mail Assíncrono (BCC)
        if (!emailsParaDisparo.isEmpty()) {
            emailService.enviarEmailEmMassa(emailsParaDisparo, evento.titulo(), evento.mensagem());
        }
    }

    private List<String> distribuirParaTodos(NotificacaoEvent evento) {
        List<String> emails = new ArrayList<>();
        List<Usuario> todosUsuarios = usuarioRepository.findAll();

        for (Usuario usuario : todosUsuarios) {
            if (Boolean.TRUE.equals(usuario.getAtivo())) {
                processarNotificacao(usuario.getId(), evento);
                if (usuario.getEmail() != null) emails.add(usuario.getEmail());
            }
        }
        return emails;
    }

    private List<String> distribuirParaRoleKeycloak(String roleName, NotificacaoEvent evento) {
        List<String> emails = new ArrayList<>();
        List<UUID> adminsIds = keycloakService.obterUsuariosPorRole(roleName);

        List<Usuario> admins = usuarioRepository.findAllById(adminsIds);
        for (Usuario admin : admins) {
            if (Boolean.TRUE.equals(admin.getAtivo())) {
                processarNotificacao(admin.getId(), evento);
                if (admin.getEmail() != null) emails.add(admin.getEmail());
            }
        }
        return emails;
    }

    private List<String> distribuirParaPerfilBanco(String nomePerfil, NotificacaoEvent evento) {
        List<String> emails = new ArrayList<>();
        Class<? extends Perfil> classePerfil = mapearNomeParaClasse(nomePerfil);

        if (classePerfil != null) {
            List<Usuario> destinatarios = usuarioRepository.findByPerfilType(classePerfil);
            for (Usuario usuario : destinatarios) {
                if (Boolean.TRUE.equals(usuario.getAtivo())) {
                    processarNotificacao(usuario.getId(), evento);
                    if (usuario.getEmail() != null) emails.add(usuario.getEmail());
                }
            }
        } else {
            log.warn("Classe de entidade de perfil não encontrada para a string: {}", nomePerfil);
        }
        return emails;
    }

    private void processarNotificacao(UUID usuarioId, NotificacaoEvent evento) {
        redisService.guardarNotificacaoOffline(usuarioId, evento);
        sseService.emitirSinalDeNovaNotificacao(usuarioId);
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