package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;


public class CursoNotFoundException extends NotFoundException {
    public CursoNotFoundException() {
        super("Curso não encontrado");
    }
}