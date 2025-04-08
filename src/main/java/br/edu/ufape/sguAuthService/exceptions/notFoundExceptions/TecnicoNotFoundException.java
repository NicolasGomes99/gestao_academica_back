package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class TecnicoNotFoundException extends NotFoundException {
    public TecnicoNotFoundException() {
        super("Tecnico não encontrado");
    }
}
