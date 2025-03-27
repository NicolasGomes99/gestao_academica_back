package br.edu.ufape.sguAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Gestor extends Funcionario {

    @OneToOne
    @JoinColumn(name = "unidade_administrativa_id", unique = true)
    private UnidadeAdministrativa unidadeAdministrativa;

}
