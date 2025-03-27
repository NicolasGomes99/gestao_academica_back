package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.EstudanteRepository;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.models.Estudante;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudanteService implements br.edu.ufape.sguAuthService.servicos.interfaces.EstudanteService {
    private final EstudanteRepository estudanteRepository;
    private final ModelMapper modelMapper;

    public Estudante salvarEstudante(Estudante estudante) {
        if (!estudante.isDeficiente()) {
            estudante.setTipoDeficiencia(null);
        }
        return estudanteRepository.save(estudante);
    }

    public Estudante buscarEstudante(Long id) throws AlunoNotFoundException{
        return estudanteRepository.findById(id).orElseThrow(AlunoNotFoundException::new);
    }

    public List<Estudante> listarEstudantes() {
        return estudanteRepository.findAll();
    }

    public Estudante atualizarEstudante(Long id, Estudante estudante) throws AlunoNotFoundException{
        Estudante estudanteExistente = estudanteRepository.findById(id).orElseThrow(AlunoNotFoundException::new);
        if (estudanteExistente != null) {
            modelMapper.map(estudante, estudanteExistente);
            if (!estudanteExistente.isDeficiente()) {
                estudanteExistente.setTipoDeficiencia(null);
            }
            return estudanteRepository.save(estudanteExistente);
        }
        return null;
    }

    public void deletarEstudante(Long id) {
        estudanteRepository.deleteById(id);
    }
}