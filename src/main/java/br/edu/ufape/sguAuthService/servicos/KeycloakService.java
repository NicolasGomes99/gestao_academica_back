package br.edu.ufape.sguAuthService.servicos;



import br.edu.ufape.sguAuthService.comunicacao.dto.auth.TokenResponse;
import br.edu.ufape.sguAuthService.exceptions.auth.KeycloakAuthenticationException;
import br.edu.ufape.sguAuthService.servicos.interfaces.KeycloakServiceInterface;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service @RequiredArgsConstructor
public class KeycloakService implements KeycloakServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${common.emailEnabled}")
    private boolean emailEnabled;



    @Override
    @PostConstruct
    public void init() {
        System.out.println(keycloakServerUrl);
        // Inicialize o cliente Keycloak
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl) // URL do servidor Keycloak
                .realm("master") // Realm do admin
                .clientId("admin-cli")
                .username("admin") // Credenciais do administrador
                .password("admin")
                .build();
    }

    @Override
    public TokenResponse login(String email, String password) throws KeycloakAuthenticationException {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        // Fazer a requisição HTTP para o token endpoint do Keycloak
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Parâmetros da requisição
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", email);
        formData.add("password", password);

        // Fazer a requisição
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, TokenResponse.class);
            // Verificar se a requisição foi bem sucedida e retornar o token adicionando as roles no response
            if (response.getStatusCode() == HttpStatus.OK) {
                TokenResponse tokenResponse = response.getBody();
                String userId = getUserId(email);
                List<RoleRepresentation> roles = keycloak.realm(realm).users().get(userId).roles().realmLevel().listEffective();
                assert tokenResponse != null;
                tokenResponse.setRoles(roles.stream().map(RoleRepresentation::getName).toList());
                return tokenResponse;
            }
            // Retorno de status diferente de OK


        } catch (HttpStatusCodeException e) {
            // Captura qualquer erro HTTP e retorna uma mensagem apropriada com base no status
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                if (!verifyEmailValid(email)){
                    throw new KeycloakAuthenticationException("E-mail não verificado. Verifique sua caixa de entrada e clique no link de verificação.");
                }
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Credenciais inválidas. Verifique o email e a senha. Erro: {}", e, e);
                throw new KeycloakAuthenticationException("Credenciais inválidas. Verifique o email e a senha.");
            }
            throw new KeycloakAuthenticationException("Erro ao autenticar no Keycloak: " + e.getStatusCode(), e);

        } catch (ResourceAccessException e) {
            // Captura erros de conexão ou timeout
            throw new KeycloakAuthenticationException("Não foi possível acessar o servidor Keycloak. Verifique sua conexão.", e);

        } catch (Exception e) {
            // Captura qualquer outro erro inesperado
            throw new KeycloakAuthenticationException("Erro inesperado durante o login.", e);
        }
        throw new KeycloakAuthenticationException("Erro ao autenticar: resposta inesperada do servidor.");
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String keycloakTokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(keycloakTokenUrl, request, TokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Retorna o novo access_token e refresh_token
        } else {
            throw new RuntimeException("Failed to refresh token");
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        String logoutUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new KeycloakAuthenticationException("Um erro ao fazer logout no Keycloak. Status: " + response.getStatusCode());
        }
    }

    @Override
    public void createUser(String email, String password, String role) throws  KeycloakAuthenticationException {
        String userId = null;
        try {
            // Configurar as credenciais do usuário
            UserRepresentation user = getUserRepresentation(email, password);

            // Criar o usuário no Keycloak
            Response response = keycloak.realm(realm).users().create(user);

            // Verificar se a criação foi bem-sucedida
            if (response.getStatus() != 201) {
                if (response.getStatus() == 409) {
                    log.warn("Credenciais já existentes. Tente outro email. Ero: {}", response.getStatusInfo());
                    throw new KeycloakAuthenticationException("Credenciais já existentes. Tente outro email.");
                }
                log.error("Erro ao criar o usuário no Keycloak. Status: {}", response.getStatusInfo());
                throw new KeycloakAuthenticationException("Erro ao criar o usuário no Keycloak. Status: " + response.getStatus());
            }

            // Atribuir o papel (role) ao usuário
            userId = keycloak.realm(realm).users().search(email).getFirst().getId();
            RoleRepresentation userRole = keycloak.realm(realm).roles().get(role).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

            // Enviar e-mail de confirmação
            if (emailEnabled){
                try {
                    List<String> actions = Collections.singletonList("VERIFY_EMAIL");
                    keycloak.realm(realm).users().get(userId).executeActionsEmail(actions);
                } catch (Exception e){
                    log.error("Erro ao enviar e-mail de confirmação para o usuário {}: {}", email, e.getMessage());
                    throw new KeycloakAuthenticationException(e.getMessage());
                }

            }


        } catch (NotFoundException e) {
            log.error("Erro: {} ",e, e);
            if (userId != null) {
                deleteUser(userId);
            }
            throw new KeycloakAuthenticationException("Role " + role + " não encontrado no Keycloak.", e);

        } catch (KeycloakAuthenticationException e) {
            log.error("Erro: {}", e, e);
            if (userId != null) {
                deleteUser(userId);
            }
            throw e;  // Exceção já personalizada, não precisa de novo tratamento

        } catch (Exception e) {
            log.error("Erro inesperado ao criar o usuário no Keycloak.{}", e, e);
            if (userId != null) {
                deleteUser(userId);
            }
            throw new KeycloakAuthenticationException("Erro inesperado ao criar o usuário no Keycloak.", e);
        }
    }

    @Override
    public void addRoleToUser(String userId, String role) {
        try {
            RoleRepresentation userRole = keycloak.realm(realm).roles().get(role).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));
            log.info("Papel {} adicionado ao usuário", role);
        } catch (NotFoundException e) {
            log.error("Ocorreu um erro {}", e,e);
            throw new KeycloakAuthenticationException("Role " + role + " não encontrado no Keycloak.", e);
        }catch (Exception e) {
            log.error("Erro inesperado ao adicionar papel ao usuário.{}", e, e);
            throw new KeycloakAuthenticationException("Erro inesperado ao adicionar papel ao usuário." + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
        log.info("Usuário excluído com sucesso.");
    }



    @Override
    public String getUserId(String username) {
        List<UserRepresentation> user = keycloak.realm(realm).users().search(username, true);
        if (!user.isEmpty()) {
            return user.getFirst().getId();
        }
        log.error("Usuário não encontrado.");
        throw new IndexOutOfBoundsException("User not found");
    }

    @Override
    public boolean hasRoleAdmin(String accessToken) {
        try {
            return keycloak.realm(realm).users().get(accessToken).roles().realmLevel().listEffective().stream().anyMatch(role -> role.getName().equals("administrador"));
        } catch (Exception e) {
            log.error("Erro ao verificar se o usuário tem a role de administrador.{}", e, e);
            throw new KeycloakAuthenticationException("Erro ao verificar se o usuário tem a role de administrador.", e);
        }
    }

    @Override
    public void resetPassword(String email) throws KeycloakAuthenticationException {
        try {
            // Busca o usuário pelo email
            List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);

            if (users == null || users.isEmpty()) {
                throw new KeycloakAuthenticationException("Usuário com email " + email + " não encontrado.");
            }

            UserRepresentation user = users.getFirst();
            String userId = user.getId();

            log.info("Enviando e-mail de redefinição de senha para o usuário: {}", email);
            List<String> actions = Collections.singletonList("UPDATE_PASSWORD");
            keycloak.realm(realm).users().get(userId).executeActionsEmail(actions);

            // Log de sucesso
            log.info("E-mail de redefinição de senha enviado com sucesso para o usuário: {}", email);

        } catch (KeycloakAuthenticationException e) {
            log.error("Erro: {}", e, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao processar a solicitação de redefinição de senha.{}", e, e);
            throw new KeycloakAuthenticationException("Erro ao processar a solicitação de redefinição de senha.", e);
        }
    }

    private boolean verifyEmailValid(String email){
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);

        Optional<UserRepresentation> userOptional = users.stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst();

        if (userOptional.isPresent()) {
            UserRepresentation user = userOptional.get();

            return user.isEmailVerified();
        }
        return true;
    }

    private UserRepresentation getUserRepresentation(String email, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        // Configurar o novo usuário
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setFirstName(email);
        user.setLastName(email);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(!emailEnabled);
        user.setCredentials(Collections.singletonList(credential));


        return user;
    }

    @Override
    public List<UserRepresentation> listUnverifiedUsers() {
        return keycloak.realm(realm).users().list().stream()
                .filter(user -> !user.isEmailVerified())
                .collect(Collectors.toList());
    }

    @Override
    public void addClientRoleToUser(String userId, String clientId, String roleName) {
        try {
            ClientRepresentation client = keycloak.realm(realm)
                    .clients()
                    .findByClientId(clientId)
                    .getFirst();

            RoleRepresentation role = keycloak.realm(realm)
                    .clients()
                    .get(client.getId())
                    .roles()
                    .get(roleName)
                    .toRepresentation();

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .clientLevel(client.getId())
                    .add(Collections.singletonList(role));

            log.info("Adicionada role '{}' do client '{}' ao usuário '{}'", roleName, clientId, userId);

        } catch (Exception e) {
            log.error("Erro ao adicionar role de client ao usuário", e);
            throw new KeycloakAuthenticationException("Erro ao adicionar role de client ao usuário", e);
        }
    }

    @Override
    public void addUserToGroup(String userId, String groupName) {
        log.info("Adicionando usuario '{}' ao grupo '{}'", userId, groupName);
        Optional<GroupRepresentation> groupOptional = findGroupByNameRecursive(groupName);
        if (groupOptional.isEmpty()) {
            log.warn("Aviso: Grupo não encontrado: {}", groupName); // Alterado para warn e isEmpty
            return;
        }

        keycloak.realm(realm)
                .users()
                .get(userId)
                .joinGroup(groupOptional.get().getId());
        log.info("Usuário '{}' adicionado ao grupo '{}' (ID: {})", userId, groupName, groupOptional.get().getId());
    }

    @Override
    public void removeUserFromGroup(String userId, String groupName) {
        log.info("Removendo usuario '{}' do grupo '{}'", userId, groupName);
        Optional<GroupRepresentation> groupOptional = findGroupByNameRecursive(groupName);
        if (groupOptional.isEmpty()) {
            log.warn("Aviso: Grupo não encontrado: {}", groupName); // Alterado para warn e isEmpty
            return;
        }

        keycloak.realm(realm)
                .users()
                .get(userId)
                .leaveGroup(groupOptional.get().getId());
        log.info("Usuário '{}' removido do grupo '{}' (ID: {})", userId, groupName, groupOptional.get().getId());
    }

    private Optional<GroupRepresentation> findGroupByNameRecursive(String groupName) {
        log.debug("Procurando pelo grupo ou subgrupo: {}", groupName);
        // Obtém todos os grupos de nível superior
        List<GroupRepresentation> topLevelGroups = keycloak.realm(realm).groups().groups("", 0, Integer.MAX_VALUE); // Busca todos os grupos no realm

        for (GroupRepresentation group : topLevelGroups) {
            Optional<GroupRepresentation> foundGroup = findInGroupHierarchy(group, groupName);
            if (foundGroup.isPresent()) {
                return foundGroup;
            }
        }
        log.debug("Grupo ou subgrupo '{}' não encontrado no realm.", groupName);
        return Optional.empty();
    }

    private Optional<GroupRepresentation> findInGroupHierarchy(GroupRepresentation currentGroup, String targetGroupName) {
        if (Objects.equals(currentGroup.getName(), targetGroupName)) {
            log.info("Grupo '{}' (ID: {}) encontrado.", currentGroup.getName(), currentGroup.getId());
            return Optional.of(currentGroup);
        }

        List<GroupRepresentation> subGroups = currentGroup.getSubGroups();
        if (subGroups != null && !subGroups.isEmpty()) {
            log.trace("Verificando subgrupos de '{}' (ID: {}), total de subgrupos: {}", currentGroup.getName(), currentGroup.getId(), subGroups.size());
            for (GroupRepresentation subGroup : subGroups) {
                // Chamada recursiva para cada subgrupo
                Optional<GroupRepresentation> foundInSubGroup = findInGroupHierarchy(subGroup, targetGroupName);
                if (foundInSubGroup.isPresent()) {
                    return foundInSubGroup;
                }
            }
        } else {
            log.trace("Grupo '{}' (ID: {}) não possui subgrupos preenchidos ou não tem subgrupos.", currentGroup.getName(), currentGroup.getId());
        }

        return Optional.empty();
    }


    private GroupRepresentation findGroupByName(String groupName) {
        List<GroupRepresentation> matches = keycloak
                .realm(realm)
                .groups()
                .groups(groupName, true, 0, 1000, false);

        if (!matches.isEmpty()) {
            for (GroupRepresentation match : matches) {
                log.info("Group '{}' encontrado", match.getName());
            }
        }
        return matches.stream()
                .filter(g -> groupName.equals(g.getName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<String> getUserRoles(String userId) {
        try {
            List<String> realmRoles = keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .listEffective()
                    .stream()
                    .map(RoleRepresentation::getName)
                    .toList();

            List<ClientRepresentation> clients = keycloak.realm(realm).clients().findAll();

            List<String> clientRoles = new ArrayList<>();
            for (ClientRepresentation client : clients) {
                List<RoleRepresentation> roles = keycloak.realm(realm)
                        .users()
                        .get(userId)
                        .roles()
                        .clientLevel(client.getId())
                        .listEffective();

                clientRoles.addAll(
                        roles.stream()
                                .map(RoleRepresentation::getName)
                                .toList()
                );
            }

            List<String> allRoles = new ArrayList<>();
            allRoles.addAll(realmRoles);
            allRoles.addAll(clientRoles);

            return allRoles;
        } catch (Exception e) {
            log.error("Erro ao obter as roles do usuário: {}", e.getMessage(), e);
            throw new KeycloakAuthenticationException("Erro ao obter as roles do usuário.", e);
        }
    }

}