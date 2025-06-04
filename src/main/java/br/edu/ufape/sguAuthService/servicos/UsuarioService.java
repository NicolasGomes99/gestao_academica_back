package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;


import br.edu.ufape.sguAuthService.exceptions.accessDeniedException.GlobalAccessDeniedException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.models.Aluno;
import br.edu.ufape.sguAuthService.models.Usuario;

import br.edu.ufape.sguAuthService.models.Visitante;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class UsuarioService implements br.edu.ufape.sguAuthService.servicos.interfaces.UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Override
    public Usuario salvar(Usuario usuario) {
        Visitante visitante = new Visitante();
        usuario.adicionarPerfil(visitante);
        return usuarioRepository.save(usuario);
    }
    @Override
    public Usuario editarUsuario(Usuario novoUsuario) throws UsuarioNotFoundException {
        UUID idSessao = authenticatedUserProvider.getUserId();
        Usuario antigoUsuario =  usuarioRepository.findById(idSessao).orElseThrow(UsuarioNotFoundException::new);
        modelMapper.map(novoUsuario, antigoUsuario);
        return usuarioRepository.save(antigoUsuario);
    }

    @Override
    public Usuario buscarUsuario(UUID id, boolean isAdm, UUID sessionId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNotFoundException::new);
        if(!isAdm && !usuario.getId().equals(sessionId)) {
            throw new GlobalAccessDeniedException("Você não tem permissão para acessar este recurso");
        }
        return usuario;
    }

    @Override
    public Usuario buscarUsuarioAtual() throws UsuarioNotFoundException{
        UUID idSessao = authenticatedUserProvider.getUserId();
        return usuarioRepository.findById(idSessao).orElseThrow(UsuarioNotFoundException::new);
    }

//    @Override
//    public List<Usuario> listarUsuarios() {
//        return usuarioRepository.findByAtivoTrue();
//    }

    @Override
    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findByAtivoTrue(pageable);
    }


    @Override
    public void deletarUsuario(UUID sessionId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(sessionId).orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> buscarUsuariosPorIds(List<UUID> kcIds) {
        List<Usuario> usuarios = usuarioRepository.findByIdIn(kcIds);

        Map<UUID, Usuario> usuarioMap = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getId, Function.identity()));

        List<Usuario> users =  kcIds.stream()
                .map(usuarioMap::get)
                .collect(Collectors.toList());

        logger.info("Usuarios encontrados: {}", users.getFirst().getPerfil(Aluno.class));
        return users;
    }



}
