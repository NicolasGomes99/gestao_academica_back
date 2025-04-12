package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;


public class UsuarioNotFoundException extends NotFoundException {
    public UsuarioNotFoundException() {
        super("Usuario não encontrada");
    }
}