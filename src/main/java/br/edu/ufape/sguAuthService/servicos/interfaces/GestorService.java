package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Usuario;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GestorService {

    Page<Usuario> listarGestores(Predicate predicate, Pageable pageable);

    Usuario buscarGestor(UUID id, boolean isAdm, UUID sessionId) throws GestorNotFoundException, UsuarioNotFoundException;
}
