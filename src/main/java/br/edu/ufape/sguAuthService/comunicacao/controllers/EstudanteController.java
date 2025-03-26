package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.estudante.EstudanteResponse;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Estudante;
import br.edu.ufape.sguAuthService.servicos.interfaces.EstudanteService;
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
                .map(EstudanteResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudanteResponse> buscarEstudante(@PathVariable Long id) {
        Estudante estudante = fachada.buscarEstudante(id);
        return ResponseEntity.ok(new EstudanteResponse(estudante));
    }

    @PostMapping()
    public ResponseEntity<EstudanteResponse> criarEstudante(@RequestBody Estudante estudante) {
        Estudante novoEstudante = fachada.salvarEstudante(estudante);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EstudanteResponse(novoEstudante));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstudanteResponse> atualizarEstudante(@PathVariable Long id, @Valid @RequestBody Estudante estudante) {
        Estudante estudanteAtualizado = fachada.atualizarEstudante(id, estudante);
        return ResponseEntity.ok(new EstudanteResponse(estudanteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstudante(@PathVariable Long id) {
        fachada.deletarEstudante(id);
        return ResponseEntity.noContent().build();
    }
}