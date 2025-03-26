package br.edu.ufape.sguAuthService.servicos.interfaces;

import br.edu.ufape.sguAuthService.models.DadosBancarios;

import java.util.List;

public interface DadosBancariosService {

    DadosBancarios buscarDadosBancarios(Long id);

    List<DadosBancarios> listarDadosBancarios();

    DadosBancarios salvarDadosBancarios(DadosBancarios dadosBancarios);

    DadosBancarios atualizarDadosBancarios(Long id, DadosBancarios novosDadosBancarios);

    void deletarDadosBancarios(Long id);
}
