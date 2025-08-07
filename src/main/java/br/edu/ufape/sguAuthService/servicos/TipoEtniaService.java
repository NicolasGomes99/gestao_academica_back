package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.TipoEtniaRepository;
import br.edu.ufape.sguAuthService.exceptions.TipoEtniaDuplicadoException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoEtniaService implements br.edu.ufape.sguAuthService.servicos.interfaces.TipoEtniaService{
    private final  TipoEtniaRepository tipoEtniaRepository;

    @Override
    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        tipoEtniaRepository.findByTipoIgnoreCase(tipoEtnia.getTipo())
                .ifPresent(_ -> {
                    throw new TipoEtniaDuplicadoException("Já existe um tipo de etnia: " + tipoEtnia.getTipo());
                });

        return tipoEtniaRepository.save(tipoEtnia);
    }

    @Override
    public TipoEtnia buscarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        return tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
    }

    @Override
    public Page<TipoEtnia> listarTiposEtnia(Predicate predicate, Pageable pageable) {
        return tipoEtniaRepository.findAll(predicate, pageable);
    }

    @Override
    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id)
                .orElseThrow(TipoEtniaNotFoundException::new);

        tipoEtniaRepository.findByTipoIgnoreCase(tipoEtnia.getTipo())
                .ifPresent(et -> {
                    if (!et.getId().equals(id)) {
                        throw new TipoEtniaDuplicadoException("Já existe um tipo de etnia: " + tipoEtnia.getTipo());
                    }
                });

        tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
        return tipoEtniaRepository.save(tipoEtniaExistente);
    }

    @Override
    public void deletarTipoEtnia(Long id) throws TipoEtniaNotFoundException {
        tipoEtniaRepository.findById(id).orElseThrow(TipoEtniaNotFoundException::new);
        tipoEtniaRepository.deleteById(id);
    }
}


