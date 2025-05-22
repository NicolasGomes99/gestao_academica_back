package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;

public class FuncionarioNotFoundException extends NotFoundException {

    public FuncionarioNotFoundException() {
        super("Funcionário não encontrado");
    }
}
