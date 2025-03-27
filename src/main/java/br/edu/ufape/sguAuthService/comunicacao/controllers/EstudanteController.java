package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.estudante.EstudanteRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.AlunoNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Estudante;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estudantes")
@RequiredArgsConstructor
public class EstudanteController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<EstudanteResponse> listarEstudantes() {
        return fachada.listarEstudantes().stream()
                .map(estudante -> new EstudanteResponse(estudante, modelMapper))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudanteResponse> buscarEstudante(@PathVariable Long id) throws AlunoNotFoundException {
        Estudante estudante = fachada.buscarEstudante(id);
        if (estudante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new EstudanteResponse(estudante, modelMapper));
    }

    @PostMapping("/registrar")
    public ResponseEntity<EstudanteResponse> criarEstudante(@Valid @RequestBody EstudanteRequest estudanteRequest) {
        Estudante estudante = estudanteRequest.convertToEntity(estudanteRequest, modelMapper);
        Estudante novoEstudante = fachada.salvarEstudante(estudante);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EstudanteResponse(novoEstudante, modelMapper));
    }

    @PatchMapping("/{id}/editar")
    public ResponseEntity<EstudanteResponse> atualizarEstudante(@PathVariable Long id, @Valid @RequestBody EstudanteRequest estudanteRequest) throws AlunoNotFoundException{
        Estudante estudante = estudanteRequest.convertToEntity(estudanteRequest, modelMapper);
        Estudante estudanteAtualizado = fachada.atualizarEstudante(id, estudante);
        if (estudanteAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new EstudanteResponse(estudanteAtualizado, modelMapper));
    }

    @DeleteMapping("/{id}/deletar")
    public ResponseEntity<Void> deletarEstudante(@PathVariable Long id) {
        fachada.deletarEstudante(id);
        return ResponseEntity.noContent().build();
    }
}