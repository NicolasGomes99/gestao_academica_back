package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.dados.GestorUnidadeRepository;
import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.exceptions.ExceptionUtil;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TecnicoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaCircularException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaComDependenciasException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UnidadeAdministrativaService implements br.edu.ufape.sguAuthService.servicos.interfaces.UnidadeAdministrativaService {
    private final UnidadeAdministrativaRepository unidadeAdministrativaRepository;

    private final ModelMapper modelMapper;
    private final GestorUnidadeRepository gestorUnidadeRepository;

    @Override
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

    @Override
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
    public Page<UnidadeAdministrativa> listarUnidadesAdministrativas(Pageable pageable) {
        return unidadeAdministrativaRepository.findAll(pageable);
    }

    @Override
    public List<UnidadeAdministrativa> montarArvore() {
        return unidadeAdministrativaRepository.findByUnidadePaiIsNull();
    }

    @Override
    public List<UnidadeAdministrativa> listarUnidadesFilhas(Long id) {
        return unidadeAdministrativaRepository.findByUnidadePaiId(id);
    }

    @Override
    public void deletarUnidadeAdministrativa(Long id) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(id)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        if (!unidade.getUnidadesFilhas().isEmpty()) {
            throw new UnidadeAdministrativaComDependenciasException("Não é possível excluir a unidade, pois ela possui unidades filhas.");
        }

        unidadeAdministrativaRepository.deleteById(id);
    }

    @Override
    public GestorUnidade adicionarGestor(UnidadeAdministrativa unidade, GestorUnidade gestorUnidade) {
        gestorUnidade.setUnidadeAdministrativa(unidade);
        unidade.getGestores().add(gestorUnidade);
        gestorUnidadeRepository.save(gestorUnidade);
        return gestorUnidade;
    }

    @Override
    public void removerGestor(UnidadeAdministrativa unidade, Long gestorUnidadeId) {
        GestorUnidade gestorUnidade = unidade.getGestores().stream()
                .filter(gu -> gu.getGestor().getId().equals(gestorUnidadeId))
                .findFirst()
                .orElseThrow(GestorNotFoundException::new);

        unidade.getGestores().remove(gestorUnidade);

        gestorUnidade.setUnidadeAdministrativa(null);

        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public void adicionarFuncionario(UnidadeAdministrativa unidade, Usuario usuario) {

        Funcionario funcionario = usuario.getPerfil(Funcionario.class)
                .orElseThrow(() -> new RuntimeException("Usuário não é um funcionário."));

        if (unidade.getFuncionarios().contains(funcionario)) {
            throw new DataIntegrityViolationException("O Funcionário já está vinculado a esta unidade.");
        }

        unidade.getFuncionarios().add(funcionario);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public void removerFuncionario(UnidadeAdministrativa unidade, Usuario usuario) {
        Funcionario funcionario = usuario.getPerfil(Funcionario.class)
                .orElseThrow(() -> new RuntimeException("Usuário não é um funcionário."));

        if (!unidade.getFuncionarios().contains(funcionario)) {
            throw new TecnicoNotFoundException();
        }
        unidade.getFuncionarios().remove(funcionario);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public Set<GestorUnidade> listarGestores(Long unidadeId) {
        UnidadeAdministrativa unidade = buscarUnidadeAdministrativa(unidadeId);
        return unidade.getGestores();
    }

    @Override
    public Set<Funcionario> listarFuncionarios(Long unidadeId) {
        UnidadeAdministrativa unidade = buscarUnidadeAdministrativa(unidadeId);
        return unidade.getFuncionarios();
    }

    @Override
    public List<UnidadeAdministrativa> listarUnidadesPorGestor(Gestor gestor) {
        Long gestorId = gestor.getId();
        List<GestorUnidade> vinculacoes = gestorUnidadeRepository.findByGestorId(gestorId);
        return vinculacoes.stream()
                .map(GestorUnidade::getUnidadeAdministrativa)
                .distinct()
                .toList();
    }

    @Override
    public List<UnidadeAdministrativa> listarUnidadesPorFuncionario(Usuario usuario) {
        return unidadeAdministrativaRepository.findAll().stream()
                .filter(ua -> ua.getFuncionarios().stream()
                        .anyMatch(f -> usuario.getPerfis().contains(f) &&
                                (f instanceof Tecnico || f instanceof Professor)))
                .distinct()
                .toList();
    }

}