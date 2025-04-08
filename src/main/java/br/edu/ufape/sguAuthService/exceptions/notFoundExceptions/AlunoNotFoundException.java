package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class AlunoNotFoundException extends NotFoundException {
    public AlunoNotFoundException() {
        super("Aluno não encontrado");
    }
}