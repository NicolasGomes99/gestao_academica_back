package br.edu.ufape.sguAuthService.comunicacao.dto.documento;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DocumentoResponse {
    private String nome;

    private String base64;
}
