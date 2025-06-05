package br.edu.ufape.sguAuthService.servicos.interfaces;

import java.util.List;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoUnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoUnidadeAdministrativaService {

    TipoUnidadeAdministrativa salvar(TipoUnidadeAdministrativa tipoUnidadeAdministrativa);

    TipoUnidadeAdministrativa buscar(Long id) throws TipoUnidadeAdministrativaNotFoundException;

    Page<TipoUnidadeAdministrativa> listar(Pageable pageable);

    TipoUnidadeAdministrativa editar(Long id, TipoUnidadeAdministrativa novoTipoUnidadeAdministrativa) throws TipoUnidadeAdministrativaNotFoundException;

    void deletar(Long id) throws TipoUnidadeAdministrativaNotFoundException;
}
