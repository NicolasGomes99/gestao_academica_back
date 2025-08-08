package br.edu.ufape.sguAuthService.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnidadeAdministrativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_unidade_administrativa_id",referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private TipoUnidadeAdministrativa tipoUnidadeAdministrativa;

    @ManyToOne
    @JoinColumn(name = "unidade_pai_id")
    @JsonBackReference
    private UnidadeAdministrativa unidadePai;

    @OneToMany(mappedBy = "unidadePai", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<UnidadeAdministrativa> unidadesFilhas = new ArrayList<>();

    @OneToMany(mappedBy = "unidadeAdministrativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GestorUnidade> gestores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "ua_funcionarios",
            joinColumns = @JoinColumn(name = "ua_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private Set<Funcionario> funcionarios = new HashSet<>();

    public void adicionarFuncionario(Funcionario f) {
        this.funcionarios.add(f);
        f.getUnidades().add(this);
    }

    public void removerFuncionario(Funcionario f) {
        this.funcionarios.remove(f);
        f.getUnidades().remove(this);
    }

}