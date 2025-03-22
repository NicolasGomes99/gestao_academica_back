package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.TipoUnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoUnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaCircularException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaComDependenciasException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaDuplicadaException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UnidadeAdministrativaService implements br.edu.ufape.sguAuthService.servicos.interfaces.UnidadeAdministrativaService {
    private final UnidadeAdministrativaRepository unidadeAdministrativaRepository;
    private final TipoUnidadeAdministrativaRepository tipoUnidadeAdministrativaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa, Long paiId) {
        if (unidadeAdministrativaRepository.existsByCodigo(unidadeAdministrativa.getCodigo())) {
            throw new UnidadeAdministrativaDuplicadaException("Código da unidade administrativa já está em uso.");
        }

        if (unidadeAdministrativa.getId() != null && unidadeAdministrativa.getId().equals(paiId)) {
            throw new UnidadeAdministrativaCircularException();
        }

        TipoUnidadeAdministrativa tipoUnidadeAdministrativa = tipoUnidadeAdministrativaRepository
                .findById(unidadeAdministrativa.getTipoUnidadeAdministrativa().getId())
                .orElseThrow(() -> new TipoUnidadeAdministrativaNotFoundException("Tipo de Unidade Administrativa não encontrado."));

        unidadeAdministrativa.setTipoUnidadeAdministrativa(tipoUnidadeAdministrativa);

        if (paiId != null) {
            UnidadeAdministrativa parent = unidadeAdministrativaRepository.findById(paiId)
                    .orElseThrow(UnidadeAdministrativaNotFoundException::new);
            unidadeAdministrativa.setUnidadePai(parent);
        }

        return unidadeAdministrativaRepository.save(unidadeAdministrativa);
    }

    @Override
    public UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa novaUnidadeAdministrativa, Long id) {
        UnidadeAdministrativa unidadeAdministrativaAtual = unidadeAdministrativaRepository.findById(id)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        if (novaUnidadeAdministrativa.getUnidadePai() != null
                && novaUnidadeAdministrativa.getUnidadePai().getId().equals(id)) {
            throw new UnidadeAdministrativaCircularException();
        }

        modelMapper.map(novaUnidadeAdministrativa, unidadeAdministrativaAtual);
        return unidadeAdministrativaRepository.save(unidadeAdministrativaAtual);
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
    public void adicionarGestor(Long unidadeId, Long gestorId) throws UsuarioNotFoundException {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
                .orElseThrow(() -> new UnidadeAdministrativaNotFoundException("Unidade administrativa não encontrada"));

        Gestor gestor = usuarioRepository.findById(gestorId)
                .orElseThrow(UsuarioNotFoundException::new)
                .getGestor();

        if (unidade.getGestor() != null) {
            throw new IllegalStateException("Esta unidade já possui um gestor.");
        }

        if (gestor.getUnidadeAdministrativa() != null) {
            throw new IllegalStateException("Este gestor já está gerenciando outra unidade.");
        }

        unidade.setGestor(gestor);
        gestor.setUnidadeAdministrativa(unidade);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public void removerGestor(Long unidadeId) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
                .orElseThrow(() -> new UnidadeAdministrativaNotFoundException("Unidade administrativa não encontrada"));

        if (unidade.getGestor() == null) {
            throw new IllegalStateException("Esta unidade não tem um gestor atribuído.");
        }

        Gestor gestor = unidade.getGestor();
        unidade.setGestor(null);
        gestor.setUnidadeAdministrativa(null);

        unidadeAdministrativaRepository.save(unidade);
    }

//    public void adicionarTecnico(Long unidadeId, Long tecnicoId) throws UsuarioNotFoundException {
//        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
//                .orElseThrow(() -> new UnidadeAdministrativaNotFoundException("Unidade administrativa não encontrada"));
//
//        Tecnico tecnico = usuarioRepository.findById(tecnicoId)
//                .orElseThrow(UsuarioNotFoundException::new)
//                .getTecnico();
//
//        if (tecnico.getUnidadeAdministrativa() != null) {
//            throw new IllegalStateException("O técnico já está alocado em outra unidade.");
//        }
//
//        tecnico.setUnidadeAdministrativa(unidade);
//        unidade.getTecnicos().add(tecnico);
//        unidadeAdministrativaRepository.save(unidade);
//    }
//
//    public void removerTecnico(Long unidadeId, Long tecnicoId) {
//        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
//                .orElseThrow(() -> new UnidadeAdministrativaNotFoundException("Unidade administrativa não encontrada"));
//
//        unidade.getTecnicos().removeIf(tecnico -> tecnico.getId().equals(tecnicoId));
//        unidadeAdministrativaRepository.save(unidade);
//    }

//    public List<Usuario> buscarTecnicosPorUnidade(Long unidadeId) {
//        return usuarioRepository.findTecnicosByUnidadeAdministrativa(unidadeId);
//    }
//
//    public Usuario buscarGestorPorUnidade(Long unidadeId) {
//        return usuarioRepository.findGestorByUnidadeAdministrativa(unidadeId)
//                .orElseThrow(() -> new UsuarioNotFoundException());
//    }

}
