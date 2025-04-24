package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.aluno.AlunoResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.curso.CursoRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.curso.CursoResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Curso;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController@RequiredArgsConstructor
@RequestMapping("/curso")
public class CursoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<CursoResponse> salvar(@Valid @RequestBody CursoRequest curso) {
        Curso response = fachada.salvarCurso(curso.convertToEntity(curso, modelMapper));
        return new ResponseEntity<>(new CursoResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<CursoResponse> editar(@PathVariable Long id, @Valid @RequestBody CursoRequest curso) throws CursoNotFoundException{
        Curso response = fachada.editarCurso(id, curso.convertToEntity(curso, modelMapper));
        return new ResponseEntity<>(new CursoResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> buscar(@PathVariable Long id) throws CursoNotFoundException {
        Curso response = fachada.buscarCurso(id);
        return new ResponseEntity<>(new CursoResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public List<CursoResponse> listar() {
        return fachada.listarCursos().stream().map(curso -> new CursoResponse(curso, modelMapper)).toList();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws CursoNotFoundException {
        fachada.deletarCurso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/alunos")
    public ResponseEntity<List<AlunoResponse>> listarAlunosPorCurso(@PathVariable Long id) throws CursoNotFoundException {
        List<AlunoResponse> response = fachada.listarAlunosPorCurso(id).stream().map(usuario -> new AlunoResponse(usuario, modelMapper)).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
