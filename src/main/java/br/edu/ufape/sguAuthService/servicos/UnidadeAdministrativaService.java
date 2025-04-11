package br.edu.ufape.sguAuthService.servicos;


import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.dados.UsuarioRepository;
import br.edu.ufape.sguAuthService.exceptions.ExceptionUtil;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaCircularException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaComDependenciasException;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UnidadeAdministrativaService implements br.edu.ufape.sguAuthService.servicos.interfaces.UnidadeAdministrativaService {
    private final UnidadeAdministrativaRepository unidadeAdministrativaRepository;
    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper;

    @Override
    public UnidadeAdministrativa salvar(UnidadeAdministrativa unidadeAdministrativa,TipoUnidadeAdministrativa tipoUnidadeAdministrativa, Long paiId) {
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
        }catch (DataIntegrityViolationException e){
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public UnidadeAdministrativa editarUnidadeAdministrativa(UnidadeAdministrativa novaUnidadeAdministrativa, Long id) {
        try {
            UnidadeAdministrativa unidadeAdministrativaAtual = unidadeAdministrativaRepository.findById(id)
                    .orElseThrow(UnidadeAdministrativaNotFoundException::new);

            if (novaUnidadeAdministrativa.getUnidadePai() != null
                    && novaUnidadeAdministrativa.getUnidadePai().getId().equals(id)) {
                throw new UnidadeAdministrativaCircularException();
            }

            modelMapper.map(novaUnidadeAdministrativa, unidadeAdministrativaAtual);
            return unidadeAdministrativaRepository.save(unidadeAdministrativaAtual);
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
    public void adicionarGestor(Long unidadeId, Long usuarioId) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(GestorNotFoundException::new);

        Gestor gestor = usuario.getGestor();
        unidade.setGestor(gestor);

        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public void removerGestor(Long unidadeId) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        unidade.setGestor(null);
        unidadeAdministrativaRepository.save(unidade);
    }

    @Override
    public void adicionarTecnico(Long unidadeId, Long usuarioId) {
        UnidadeAdministrativa unidade = unidadeAdministrativaRepository.findById(unidadeId)
                .orElseThrow(UnidadeAdministrativaNotFoundException::new);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Tecnico tecnico = usuario.getTecnico();

        tecnico.setUnidadeAdministrativa(unidade);
        usuarioRepository.save(usuario);
    }

    @Override
    public void removerTecnico(Long unidadeId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Tecnico tecnico = usuario.getTecnico();

        if (tecnico.getUnidadeAdministrativa() != null &&
                tecnico.getUnidadeAdministrativa().getId().equals(unidadeId)) {

            tecnico.setUnidadeAdministrativa(null);
            usuarioRepository.save(usuario);
        }
    }

//    @Override
//    public void vincularGestor(UnidadeAdministrativa unidade, Gestor gestor) {
//        unidade.setGestor(gestor);
//        unidadeAdministrativaRepository.save(unidade);
//    }
//
//    @Override
//    public void desvincularGestor(UnidadeAdministrativa unidade) {
//        unidade.setGestor(null);
//        unidadeAdministrativaRepository.save(unidade);
//    }
//
//    @Override
//    public void vincularTecnico(Tecnico tecnico, UnidadeAdministrativa unidade) {
//        tecnico.setUnidadeAdministrativa(unidade);
//    }
//
//    @Override
//    public void desvincularTecnico(Tecnico tecnico) {
//        tecnico.setUnidadeAdministrativa(null);
//    }


}
