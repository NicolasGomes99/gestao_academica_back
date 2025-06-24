package br.edu.ufape.sguAuthService.exceptions;

import com.sun.jdi.request.DuplicateRequestException;

public class SolicitacaoDuplicadaException extends DuplicateRequestException {
    public SolicitacaoDuplicadaException(String message) {
        super(message);
    }
}
