package br.edu.ufape.sguAuthService.servicos;

import java.util.List;

import br.edu.ufape.sguAuthService.exceptions.ExceptionUtil;
import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ufape.sguAuthService.dados.TipoUnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoUnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoUnidadeAdministrativaservice implements br.edu.ufape.sguAuthService.servicos.interfaces.TipoUnidadeAdministrativaService {
    private final TipoUnidadeAdministrativaRepository tipoUnidadeAdministrativaRepository;
    private final ModelMapper modelMapper;

    @Override
    public TipoUnidadeAdministrativa salvar(TipoUnidadeAdministrativa tipoUnidadeAdministrativa) {
        try {
            return tipoUnidadeAdministrativaRepository.save(tipoUnidadeAdministrativa);
        }catch (DataIntegrityViolationException e) {
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public TipoUnidadeAdministrativa buscar(Long id) throws TipoUnidadeAdministrativaNotFoundException {
        return tipoUnidadeAdministrativaRepository.findById(id).orElseThrow(TipoUnidadeAdministrativaNotFoundException::new);
    }

    @Override
    public Page<TipoUnidadeAdministrativa> listar(Predicate predicate, Pageable pageable) {
        return tipoUnidadeAdministrativaRepository.findAll(predicate, pageable);
    }

    @Override
    public TipoUnidadeAdministrativa editar(Long id, TipoUnidadeAdministrativa novoTipoUnidadeAdministrativa) throws TipoUnidadeAdministrativaNotFoundException {
        try {
            TipoUnidadeAdministrativa antigoTipoUnidadeAdministrativa = tipoUnidadeAdministrativaRepository.findById(id).orElseThrow(TipoUnidadeAdministrativaNotFoundException::new);
            modelMapper.map(novoTipoUnidadeAdministrativa, antigoTipoUnidadeAdministrativa);
            return tipoUnidadeAdministrativaRepository.save(antigoTipoUnidadeAdministrativa);
        }catch (DataIntegrityViolationException e) {
            throw ExceptionUtil.handleDataIntegrityViolationException(e);
        }

    }

    @Override
    public void deletar(Long id) throws TipoUnidadeAdministrativaNotFoundException {
        TipoUnidadeAdministrativa tipoUnidadeAdministrativa = tipoUnidadeAdministrativaRepository.findById(id).orElseThrow(TipoUnidadeAdministrativaNotFoundException::new);
        tipoUnidadeAdministrativaRepository.delete(tipoUnidadeAdministrativa);
    }
}
