package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.models.Estudante;
import java.util.List;

public interface EstudanteService {
    Estudante salvarEstudante(Estudante estudante);

    Estudante buscarEstudante(Long id);

    List<Estudante> listarEstudantes();

    Estudante atualizarEstudante(Long id, Estudante estudante);

    void deletarEstudante(Long id);
}