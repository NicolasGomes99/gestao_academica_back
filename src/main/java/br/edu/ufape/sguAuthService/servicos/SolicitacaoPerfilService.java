package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.config.AuthenticatedUserProvider;
import br.edu.ufape.sguAuthService.dados.SolicitacaoPerfilRepository;
import br.edu.ufape.sguAuthService.exceptions.SolicitacaoDuplicadaException;
import br.edu.ufape.sguAuthService.exceptions.SolicitacaoNaoPendenteException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.SolicitacaoNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import br.edu.ufape.sguAuthService.models.Enums.StatusSolicitacao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class SolicitacaoPerfilService implements br.edu.ufape.sguAuthService.servicos.interfaces.SolicitacaoPerfilService {
    private final SolicitacaoPerfilRepository solicitacaoPerfilRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;



    @Override
    @Transactional
    public SolicitacaoPerfil solicitarPerfil(Perfil perfil, Usuario solicitante, List<Documento> documentos) throws SolicitacaoDuplicadaException {
        SolicitacaoPerfil solicitacaoPerfil = new SolicitacaoPerfil();
        solicitacaoPerfil.setPerfil(perfil);
        solicitacaoPerfil.setSolicitante(solicitante);
        solicitacaoPerfil.setDocumentos(documentos);
        solicitacaoPerfil.setStatus(StatusSolicitacao.PENDENTE);
        solicitacaoPerfil.setDataSolicitacao(LocalDateTime.now());
        solicitacaoPerfil.setPerfilSolicitado(perfil.getClass().getSimpleName());
        List<SolicitacaoPerfil> solicitacoes = solicitacaoPerfilRepository.findBySolicitanteAndStatusIn(
                solicitacaoPerfil.getSolicitante(),
                List.of(StatusSolicitacao.PENDENTE, StatusSolicitacao.APROVADA));
        for (SolicitacaoPerfil solicitacao : solicitacoes) {
            if (solicitacao.getPerfil().getClass().equals(perfil.getClass())) {
                throw new SolicitacaoDuplicadaException(
                        "Já existe uma solicitação pendente ou aprovada para o perfil: " + perfil.getClass().getSimpleName());
            }
        }
        return solicitacaoPerfilRepository.save(solicitacaoPerfil);

    }

    @Override
    public SolicitacaoPerfil buscarSolicitacao(Long id) throws SolicitacaoNotFoundException {
        return solicitacaoPerfilRepository.findById(id)
                .orElseThrow(SolicitacaoNotFoundException::new);
    }

    @Override
    public Page<SolicitacaoPerfil> buscarSolicitacoesUsuarioAtual(Predicate predicate, Pageable pageable) {
        UUID sessionId = authenticatedUserProvider.getUserId();
        QSolicitacaoPerfil qSolicitacaoPerfil = QSolicitacaoPerfil.solicitacaoPerfil;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qSolicitacaoPerfil.solicitante.id.eq(sessionId));
        Predicate predicadoFinal = filtroFixo.and(predicate);
        return solicitacaoPerfilRepository.findAll(predicadoFinal,  pageable);
    }

    @Override
    public Page<SolicitacaoPerfil> buscarSolicitacoesPorId(UUID id, Predicate predicate, Pageable pageable) {
        QSolicitacaoPerfil qSolicitacaoPerfil = QSolicitacaoPerfil.solicitacaoPerfil;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qSolicitacaoPerfil.solicitante.id.eq(id));
        Predicate predicadoFinal = filtroFixo.and(predicate);
        return solicitacaoPerfilRepository.findAll(predicadoFinal, pageable);
    }

    @Override
    public Page<SolicitacaoPerfil> listarSolicitacoes(Predicate predicate, Pageable pageable) {
        return solicitacaoPerfilRepository.findAll(predicate, pageable);
    }

    @Override
    public Page<SolicitacaoPerfil> listarSolicitacoesPendentes(Predicate predicate, Pageable pageable) {
        QSolicitacaoPerfil qSolicitacaoPerfil = QSolicitacaoPerfil.solicitacaoPerfil;
        BooleanBuilder filtroFixo = new BooleanBuilder();
        filtroFixo.and(qSolicitacaoPerfil.status.eq(StatusSolicitacao.PENDENTE));
        Predicate predicadoFinal = filtroFixo.and(predicate);
        return solicitacaoPerfilRepository.findAll(predicadoFinal, pageable);
    }


    @Override
    @Transactional
    public SolicitacaoPerfil aceitarSolicitacao(Long id, SolicitacaoPerfil parecer) throws SolicitacaoNotFoundException, SolicitacaoNaoPendenteException {
        SolicitacaoPerfil solicitacaoPerfil = buscarSolicitacao(id);
        if (solicitacaoPerfil.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new SolicitacaoNaoPendenteException();
        }
        solicitacaoPerfil.setParecer(parecer.getParecer());
        solicitacaoPerfil.setResponsavel(parecer.getResponsavel());
        solicitacaoPerfil.setDataAvaliacao(LocalDateTime.now());
        solicitacaoPerfil.setStatus(StatusSolicitacao.APROVADA);
        solicitacaoPerfil.getSolicitante().adicionarPerfil(solicitacaoPerfil.getPerfil());
        return solicitacaoPerfilRepository.save(solicitacaoPerfil);
    }

    @Override
    @Transactional
    public SolicitacaoPerfil rejeitarSolicitacao(Long id, SolicitacaoPerfil parecer) throws SolicitacaoNotFoundException {
        SolicitacaoPerfil solicitacaoPerfil = buscarSolicitacao(id);
        if (solicitacaoPerfil.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new SolicitacaoNaoPendenteException();
        }
        solicitacaoPerfil.setParecer(parecer.getParecer());
        solicitacaoPerfil.setResponsavel(parecer.getResponsavel());
        solicitacaoPerfil.setStatus(StatusSolicitacao.REJEITADA);
        return solicitacaoPerfilRepository.save(solicitacaoPerfil);
    }

}
