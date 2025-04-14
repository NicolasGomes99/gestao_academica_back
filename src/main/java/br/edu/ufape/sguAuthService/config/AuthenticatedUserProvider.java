package br.edu.ufape.sguAuthService.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedUserProvider {

    /**
     * Retorna o UUID do usuário autenticado com base no subject (sub) do JWT.
     *
     * @throws IllegalStateException se o usuário não estiver autenticado com JWT válido
     */
    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String subject = jwtToken.getToken().getSubject();

            try {
                return UUID.fromString(subject);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Subject do JWT não é um UUID válido: " + subject);
            }
        }

        throw new IllegalStateException("Usuário não autenticado via JWT.");
    }
}