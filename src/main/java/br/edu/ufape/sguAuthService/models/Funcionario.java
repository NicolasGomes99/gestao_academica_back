package br.edu.ufape.sguAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public abstract class Funcionario extends Perfil {
    private String siape;

    @ManyToMany(mappedBy = "funcionarios")
    private Set<UnidadeAdministrativa> unidades = new HashSet<>();
}
