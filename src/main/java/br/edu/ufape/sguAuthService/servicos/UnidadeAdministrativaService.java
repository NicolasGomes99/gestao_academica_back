package br.edu.ufape.sguAuthService.servicos;



import br.edu.ufape.sguAuthService.dados.FuncionarioRepository;
import br.edu.ufape.sguAuthService.dados.GestorUnidadeRepository;
import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.exceptions.ExceptionUtil;
import br.edu.ufape.sguAuthService.exceptions.SolicitacaoDuplicadaException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TecnicoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaCircularException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaComDependenciasException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor

public class UnidadeAdministrativaService implements br.edu.ufape.sguAuthService.servicos.interfaces.UnidadeAdministrativaService {
    private final UnidadeAdministrativaRepository unidadeAdministrativaRepository;

    private final ModelMapper modelMapper;
    private final GestorUnidadeRepository gestorUnidadeRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override @Transactional
    public UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) {
        try {
            if (unidadeAdministrativa.getId() != null && unidadeAdministrativa.getId().equals(paiId)) {
                throw new UnidadeAdministrativaCircularException();
            }
            unidadeAdministrativa.setTipoUnidadeAdministrativa(tipoUnidadeAdministrativa);
            if (paiId != null) {
                UnidadeAdministrativa parent = unidadeAdministrativaRepository.findById(paiId)
                        .orElseThrow(UnidadeAdministrativaNotFoundException::new);
                unidadeAdministrativa.setUnidadePai(parent);
            }

            return unidadeAdministrativaRepository.save(unidadeAdministrativa);
        } catch (DataIntegrityViolationException e) {
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override @Transactional
    public UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa novaUnidadeAdministrativa, Long id) {
        try {
            UnidadeAdministrativa unidadeAtual = unidadeAdministrativaRepository.findById(id)
                    .orElseThrow(UnidadeAdministrativaNotFoundException::new);

            if (novaUnidadeAdministrativa.getUnidadePai() != null &&
                    novaUnidadeAdministrativa.getUnidadePai().getId().equals(id)) {
                throw new UnidadeAdministrativaCircularException();
            }

            // ModelMapper tem que ignorar campos nulos
            modelMapper.getConfiguration().setPropertyCondition(ctx -> ctx.getSource() != null);

            modelMapper.typeMap(UnidadeAdministrativa.class, UnidadeAdministrativa.class)
                    .addMappings(mapper -> {
                        mapper.skip(UnidadeAdministrativa::setGestores);
                        mapper.skip(UnidadeAdministrativa::setFuncionarios);
                        mapper.skip(UnidadeAdministrativa::setUnidadesFilhas);
                        mapper.skip(UnidadeAdministrativa::setUnidadePai);
                    });

            modelMapper.map(novaUnidadeAdministrativa, unidadeAtual);

            return unidadeAdministrativaRepository.save(unidadeAtual);
        } catch (DataIntegrityViolationException e) {
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }
    }

    @Override
    public UnidadeAdministrativa buscarUnidadeAdministrativa(Long id) {
        return unidadeAdministrativaRepository.findById(id)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);
    }

    @Override
    public List<UnidadeAdministrativa> listarUnidadesAdministrativas() {
        return unidadeAdministrativaRepository.findAll();
    }

    @Override
    public List<UnidadeAdministrativa> montarArvore() {
        return unidadeAdministrativaRepository.findByUnidadePaiIsNull();
    }

    @Override
    public List<UnidadeAdministrativa> listarUnidadesFilhas(Long id) {
        return unidadeAdministrativaRepository.findByUnidadePaiId(id);
    }

    @Override @Transactional
    public void deletarUnidadeAdministrativa(Long id) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(id)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        if (!unidade.getUnidadesFilhas().isEmpty()) {
            throw new UnidadeAdministrativaComDependenciasException("Não é possível excluir a unidade, pois ela possui unidades filhas.");
        }

        unidadeAdministrativaRepository.deleteById(id);
    }

    @Transactional
    @Override
    public GestorUnidade adicionarGestor(UnidadeAdministrativa unidade, GestorUnidade gestorUnidade) {
        if(unidade.getGestores().stream().anyMatch(g -> g.getGestor().getUsuario().getId().equals(gestorUnidade.getGestor().getUsuario().getId()))) {
            throw new SolicitacaoDuplicadaException("O gestor já está vinculado a esta unidade administrativa.");
        }
        gestorUnidade.setUnidadeAdministrativa(unidade);
        unidade.getGestores().add(gestorUnidade);
        gestorUnidadeRepository.save(gestorUnidade);
        return gestorUnidade;
    }

    @Override @Transactional
    public void removerGestor(UnidadeAdministrativa unidade, Long gestorUnidadeId) {
        GestorUnidade gestorUnidade = unidade.getGestores().stream()
                .filter(gu -> gu.getGestor().getId().equals(gestorUnidadeId))
                .findFirst()
                .orElseThrow(GestorNotFoundException::new);

        unidade.getGestores().remove(gestorUnidade);

        gestorUnidade.setUnidadeAdministrativa(null);

        unidadeAdministrativaRepository.save(unidade);
    }

    @Override @Transactional
    public void adicionarFuncionario(UnidadeAdministrativa unidade, Usuario usuario) {

        Funcionario funcionario = usuario.getPerfil(Funcionario.class)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Usuário não é um funcionário."
                        )
                );

        if (unidade.getFuncionarios().contains(funcionario)) {
            throw new SolicitacaoDuplicadaException(
                    "O funcionário já está vinculado a esta unidade administrativa.");
        }

        unidade.adicionarFuncionario(funcionario);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override @Transactional
    public void removerFuncionario(UnidadeAdministrativa unidade, Usuario usuario) {
        Funcionario funcionario = usuario.getPerfil(Funcionario.class)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Usuário não é um funcionário."
                        )
                );

        if (!unidade.getFuncionarios().contains(funcionario)) {
            throw new TecnicoNotFoundException();
        }
        unidade.removerFuncionario(funcionario);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public Page<GestorUnidade> listarGestores(Long unidadeId, Predicate predicate, Pageable pageable) {
        QGestorUnidade qGestorUnidade = QGestorUnidade.gestorUnidade;
        BooleanBuilder filtroFixo = new BooleanBuilder(qGestorUnidade.unidadeAdministrativa.id.eq(unidadeId));
        Predicate filtro = filtroFixo.and(predicate);
        return gestorUnidadeRepository.findAll(filtro, pageable);
    }

    @Override
    public Page<Funcionario> listarFuncionarios(Long unidadeId, Predicate predicate, Pageable pageable) {
        QFuncionario qFuncionario = QFuncionario.funcionario;
        BooleanBuilder filtroFixo = new BooleanBuilder(
                qFuncionario.unidades.any().id.eq(unidadeId)
        );
        Predicate filtro = filtroFixo.and(predicate);
        return funcionarioRepository.findAll(filtro, pageable);
    }

    @Override
    public Page<UnidadeAdministrativa> listarUnidadesPorGestor(Gestor gestor, Predicate predicate, Pageable pageable) {
        QUnidadeAdministrativa qUnidade = QUnidadeAdministrativa.unidadeAdministrativa;
        BooleanBuilder filtro = new BooleanBuilder(
                qUnidade.gestores
                        .any()
                        .gestor.id.eq(gestor.getId())
        );

        Predicate filtroFixo = filtro.and(predicate);
        return unidadeAdministrativaRepository.findAll(filtroFixo, pageable);
    }

    @Override
    public Page<UnidadeAdministrativa> listarUnidadesPorFuncionario(Usuario usuario, Predicate predicate, Pageable pageable) {
        QUnidadeAdministrativa qUnidade = QUnidadeAdministrativa.unidadeAdministrativa;
        List<Long> perfilIds = usuario.getPerfis()
                .stream()
                .map(Perfil::getId)
                .toList();
        BooleanBuilder filtro = new BooleanBuilder()
                .and(qUnidade.funcionarios.any().id.in(perfilIds))
                .and(qUnidade.funcionarios.any().instanceOf(Tecnico.class)
                                .or(qUnidade.funcionarios.any().instanceOf(Professor.class))
                );
        Predicate filtroFixo = filtro.and(predicate);
        return unidadeAdministrativaRepository.findAll(filtroFixo, pageable);
    }

}