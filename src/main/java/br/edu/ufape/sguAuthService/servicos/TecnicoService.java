package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TecnicoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Tecnico;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TecnicoService implements br.edu.ufape.sguAuthService.servicos.interfaces.TecnicoService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;


//    @Override
//    public List<Usuario> getTecnicos(){
//        return usuarioRepository.findUsuariosTecnicos();
//    }

    @Override
    public Page<Usuario> getTecnicos(Pageable pageable) {
        return usuarioRepository.findUsuariosTecnicos(pageable);
    }


    @Override
    public Usuario buscarTecnico(UUID id, boolean isAdm, UUID sessionId) throws TecnicoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        if (usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Tecnico)) {
            throw new TecnicoNotFoundException();
        }
        return usuario;
    }

    @Override
    public Usuario buscarTecnicoAtual() throws TecnicoNotFoundException, UsuarioNotFoundException {
        Usuario usuario =  usuarioRepository.findById(authenticatedUserProvider.getUserId()).orElseThrow(UsuarioNotFoundException::new);
        if(usuario.getPerfis().stream().noneMatch(perfil -> perfil instanceof Tecnico)){
            throw new TecnicoNotFoundException();
        }
        return usuario;
    }

}
