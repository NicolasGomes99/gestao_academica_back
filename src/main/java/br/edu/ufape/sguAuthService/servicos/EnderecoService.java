package br.edu.ufape.sguAuthService.servicos;

import br.edu.ufape.sguAuthService.dados.EnderecoRepository;
import br.edu.ufape.sguAuthService.models.Endereco;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService implements br.edu.ufape.sguAuthService.servicos.interfaces.EnderecoService{

    private final EnderecoRepository enderecoRepository;
    private final ModelMapper modelMapper;


    public List<Endereco> listarEnderecos() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> buscarEndereco(Long id) {
        return enderecoRepository.findById(id);
    }

    public Endereco criarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public void excluirEndereco(Long id) {
        enderecoRepository.deleteById(id);
    }

    public Endereco editarEndereco(Long id, Endereco enderecoAtualizado) {
        Endereco enderecoAtual = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

        modelMapper.map(enderecoAtualizado, enderecoAtual);
        return enderecoRepository.save(enderecoAtual);
    }
}
