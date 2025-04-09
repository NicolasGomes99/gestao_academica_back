package br.edu.ufape.sguAuthService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Tecnico extends Funcionario {

    @ManyToOne
    @JoinColumn(name = "unidade_administrativa_id")
    private UnidadeAdministrativa unidadeAdministrativa;

}
