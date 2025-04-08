package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class GestorNotFoundException extends NotFoundException {
    public GestorNotFoundException() {
        super("Gestor não encontrado");
    }
}
