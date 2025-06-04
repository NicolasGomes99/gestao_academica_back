package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.exceptions.SolicitacaoDuplicadaException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.SolicitacaoNotFoundException;
import br.edu.ufape.sguAuthService.models.Documento;
import br.edu.ufape.sguAuthService.models.Perfil;
import br.edu.ufape.sguAuthService.models.SolicitacaoPerfil;
import br.edu.ufape.sguAuthService.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

public interface SolicitacaoPerfilService {
    @Transactional
    SolicitacaoPerfil solicitarPerfil(Perfil perfil, Usuario solicitante, List<Documento> documentos) throws SolicitacaoDuplicadaException;

    SolicitacaoPerfil buscarSolicitacao(Long id) throws SolicitacaoNotFoundException;

    List<SolicitacaoPerfil> buscarSolicitacoesUsuarioAtual();

    List<SolicitacaoPerfil> buscarSolicitacoesPorId(UUID id);

    Page<SolicitacaoPerfil> listarSolicitacoes(Pageable pageable);

    Page<SolicitacaoPerfil> listarSolicitacoesPendentes(Pageable pageable);

    @Transactional
    SolicitacaoPerfil aceitarSolicitacao(Long id, SolicitacaoPerfil parecer) throws SolicitacaoNotFoundException;

    @Transactional
    SolicitacaoPerfil rejeitarSolicitacao(Long id, SolicitacaoPerfil parecer) throws SolicitacaoNotFoundException;
}
