package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TipoEtniaService {
    TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia);

    TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException;

    Page<TipoEtnia> listarTiposEtnia(Predicate predicate, Pageable pageable);

    TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException;

    void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException;
}
