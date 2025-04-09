package br.edu.ufape.sguAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Gestor extends Funcionario {

    @OneToOne(mappedBy = "gestor")
    private UnidadeAdministrativa unidadeAdministrativa;

}
