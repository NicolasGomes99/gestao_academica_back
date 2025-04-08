package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
