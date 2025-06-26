package br.edu.ufape.sguAuthService.comunicacao.dto.curso;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CursoPatchRequest {
    private String nome;
    private Integer numeroPeriodos;
}