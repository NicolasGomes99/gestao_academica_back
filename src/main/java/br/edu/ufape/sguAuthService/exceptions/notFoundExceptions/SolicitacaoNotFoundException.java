package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class SolicitacaoNotFoundException extends NotFoundException {
    public SolicitacaoNotFoundException() {
        super("Solicitação não encontrada");
    }
}