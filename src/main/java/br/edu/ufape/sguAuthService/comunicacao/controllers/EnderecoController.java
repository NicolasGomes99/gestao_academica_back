package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.endereco.EnderecoRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.endereco.EnderecoResponse;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Endereco;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarEndereco(@PathVariable Long id) {
        Endereco response = fachada.buscarEndereco(id);
        return new ResponseEntity<>(new EnderecoResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public List<EnderecoResponse> listarEnderecos() {
        return fachada.listarEnderecos().stream()
                .map(endereco -> new EnderecoResponse(endereco, modelMapper))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<EnderecoResponse> criarEndereco(@RequestBody EnderecoRequest enderecoRequest) {
        Endereco endereco = enderecoRequest.convertToEntity(enderecoRequest, modelMapper);
        Endereco novoEndereco = fachada.criarEndereco(endereco);
        return new ResponseEntity<>(new EnderecoResponse(novoEndereco, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EnderecoResponse> excluirEndereco(@PathVariable Long id, @RequestBody EnderecoRequest enderecoRequest) {
        Endereco endereco = enderecoRequest.convertToEntity(enderecoRequest, modelMapper);
        Endereco enderecoAtualizado = fachada.editarEndereco(id, endereco);
        if (enderecoAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new EnderecoResponse(enderecoAtualizado, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        fachada.excluirEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
