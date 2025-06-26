package br.edu.ufape.sguAuthService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Solicitação não está pendente")
public class SolicitacaoNaoPendenteException extends RuntimeException {
    public SolicitacaoNaoPendenteException() {
        super("Solicitação não está pendente");
    }
    public SolicitacaoNaoPendenteException(String message) {
        super(message);
    }
}
