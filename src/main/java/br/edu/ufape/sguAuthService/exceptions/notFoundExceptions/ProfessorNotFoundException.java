package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class ProfessorNotFoundException extends NotFoundException {
    public ProfessorNotFoundException() {
        super("Professor não encontrado");
    }
}