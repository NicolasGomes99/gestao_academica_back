package br.edu.ufape.sguAuthService.comunicacao.mensageria;

import br.edu.ufape.sguAuthService.servicos.NotificacaoSseService;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.models.Usuario;
import br.edu.ufape.sguAuthService.models.Perfil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoSseService sseService;
    private final UsuarioRepository usuarioRepository;

    @RabbitListener(queues = "sgu.notificacoes.auth.sse.queue")
    public void receberNotificacao(NotificacaoEvent evento) {

        if (evento.destinatarioId() != null) {
            // Cenário 1: Mensagem individual direta
            sseService.emitirParaUsuario(evento.destinatarioId(), evento);

        } else if (evento.perfilDestino() != null) {
            // Cenário 2: Mensagem em Massa (Broadcast por Perfil)
            log.info("Processando envio em lote para o perfil: {}", evento.perfilDestino());

            Class<? extends Perfil> classePerfil = mapearNomeParaClasse(evento.perfilDestino());

            if (classePerfil != null) {
                // Executa a query tipada que criamos no repositório
                List<Usuario> destinatarios = usuarioRepository.findByPerfilType(classePerfil);

                log.info("Encontrados {} usuários com o perfil {}", destinatarios.size(), classePerfil.getSimpleName());

                // Distribui para os canais SSE ativos de cada um deles
                for (Usuario usuario : destinatarios) {
                    sseService.emitirParaUsuario(usuario.getId(), evento);
                }
            } else {
                log.error("Não foi possível mapear o perfil destino '{}' para uma classe de entidade válida.", evento.perfilDestino());
            }
        }
    }

    /**
     * Auxiliar para converter com segurança a string do evento na classe real da herança.
     * Evita problemas de caixa (case-sensitive) ou pequenas divergências de nomenclatura.
     */
    private Class<? extends Perfil> mapearNomeParaClasse(String nomePerfil) {
        try {
            // Padroniza a string (ex: "ADMINISTRADOR" -> "Administrador", "ALUNO" -> "Aluno")
            String nomeFormatado = nomePerfil.substring(0, 1).toUpperCase() + nomePerfil.substring(1).toLowerCase();

            // Procura a classe dentro do seu pacote de models
            String fullClassName = "br.edu.ufape.sguAuthService.models." + nomeFormatado;
            Class<?> clazz = Class.forName(fullClassName);

            if (Perfil.class.isAssignableFrom(clazz)) {
                return (Class<? extends Perfil>) clazz;
            }
        } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {
            // Fallback caso a nomenclatura da classe seja diferente da role (ex: "ADMINISTRADOR" mapear para "Coordenador", etc)
            // Você pode adicionar mapeamentos explícitos fixos aqui se necessário:
            if ("ADMINISTRADOR".equalsIgnoreCase(nomePerfil)) {
                // Exemplo: se a classe de vocês se chamar Gestor ou outra específica, substitua aqui:
                // return Administrador.class;
            }
        }
        return null;
    }
}