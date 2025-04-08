package br.edu.ufape.sguAuthService.servicos.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAssignmentEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userId;
    private String clientId;
    private String role;
}