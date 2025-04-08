package br.edu.ufape.sguAuthService.exceptions.notFoundExceptions;


public class TipoUnidadeAdministrativaNotFoundException extends NotFoundException {
    public TipoUnidadeAdministrativaNotFoundException() {
        super("Tipo de Unidade não encontrada");
    }
}