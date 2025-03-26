package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.TipoEtniaRepository;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEtniaService implements br.edu.ufape.sguAuthService.servicos.interfaces.TipoEtniaService{
    private final TipoEtniaRepository tipoEtniaRepository;

    @Override
    public TipoEtnia salvarTipoEtnia(TipoEtnia tipoEtnia) {
        return tipoEtniaRepository.save(tipoEtnia);
    }

    @Override
    public TipoEtnia buscarTipoEtnia(Long id) {
        return tipoEtniaRepository.findById(id).orElse(null);
    }

    @Override
    public List<TipoEtnia> listarTiposEtnia() {
        return tipoEtniaRepository.findAll();
    }

    @Override
    public TipoEtnia atualizarTipoEtnia(Long id, TipoEtnia tipoEtnia) {
        TipoEtnia tipoEtniaExistente = tipoEtniaRepository.findById(id).orElse(null);
        if (tipoEtniaExistente != null) {
            tipoEtniaExistente.setTipo(tipoEtnia.getTipo());
            return tipoEtniaRepository.save(tipoEtniaExistente);
        }
        return null;
    }

    @Override
    public void deletarTipoEtnia(Long id) {
        tipoEtniaRepository.deleteById(id);
    }
}