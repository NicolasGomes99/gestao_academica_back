package br.edu.ufape.sguAuthService.config;

import br.edu.ufape.sguAuthService.fachada.Fachada; // 🚀 Importe sua Fachada
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt; // 🚀 Importante para pegar o ID do token
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebConfig {

    // 🚀 Injeção da Fachada
    private final Fachada fachada;

    public WebConfig(Fachada fachada) {
        this.fachada = fachada;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                                String subject = jwt.getSubject();
                                if (subject != null) {
                                    UUID userId = UUID.fromString(subject);
                                    // Chama a Fachada para limpar o SSE e evitar estouro de memória
                                    fachada.limparConexoesSse(userId);
                                }
                            }
                        })
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers("/security/**").permitAll()
                        .requestMatchers("/api-doc/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/refresh").permitAll()
                        .requestMatchers("/logout").permitAll() // Deve ser permitido
                        .requestMatchers("/reset-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tipoEtnia").permitAll()
                        .requestMatchers(HttpMethod.POST, "/aluno/public/batch").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth -> auth.jwt(token ->
                        token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

        return http.build();
    }
}