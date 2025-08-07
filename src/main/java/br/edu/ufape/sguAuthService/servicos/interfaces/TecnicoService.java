package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TecnicoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TecnicoService {
    Page<Usuario> getTecnicos(Predicate predicate, Pageable pageable);

    Usuario buscarTecnico(UUID id, boolean isAdm, UUID sessionId) throws TecnicoNotFoundException, UsuarioNotFoundException;

    Usuario buscarTecnicoAtual() throws TecnicoNotFoundException, UsuarioNotFoundException;
}
