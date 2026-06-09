package br.edu.ufape.sguAuthService.config;

import br.edu.ufape.sguAuthService.fachada.Fachada;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebConfig {

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
                                    fachada.limparConexoesSse(UUID.fromString(subject));
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
                        .requestMatchers("/logout").permitAll()
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