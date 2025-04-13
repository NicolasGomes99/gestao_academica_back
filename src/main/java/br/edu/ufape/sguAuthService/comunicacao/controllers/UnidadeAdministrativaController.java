package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa.*;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unidade-administrativa") @RequiredArgsConstructor

public class UnidadeAdministrativaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @PostMapping(value = "/registrar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UnidadeAdministrativaResponse> salvar(@Valid @RequestBody UnidadeAdministrativaRequest unidadeAdministrativaRequest) throws UnidadeAdministrativaNotFoundException {
        UnidadeAdministrativa unidade = unidadeAdministrativaRequest.convertToEntity(unidadeAdministrativaRequest, modelMapper);
        Long unidadePai = unidadeAdministrativaRequest.getUnidadePaiId();
        UnidadeAdministrativa response = fachada.salvar(unidade, unidadePai);
        return new ResponseEntity<>(new UnidadeAdministrativaResponse(response, modelMapper), HttpStatus.CREATED);
    }
     @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UnidadeAdministrativaResponse> editar(@PathVariable Long id, @Valid @RequestBody UnidadeAdministrativaPatchRequest unidadeAdministrativaPatchRequest) {
        try {
            UnidadeAdministrativa unidade = modelMapper.map(unidadeAdministrativaPatchRequest, UnidadeAdministrativa.class);
            UnidadeAdministrativa response = fachada.editarUnidadeAdministrativa(id, unidade);
            return new ResponseEntity<>(new UnidadeAdministrativaResponse(response, modelMapper), HttpStatus.OK);
        } catch (UnidadeAdministrativaNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UnidadeAdministrativaGetResponse> buscarUnidadeAdministrativa(@PathVariable Long id) throws UnidadeAdministrativaNotFoundException {
        UnidadeAdministrativa response = fachada.buscarUnidadeAdministrativa(id);
        return new ResponseEntity<>(new UnidadeAdministrativaGetResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping(value = "/listar", produces = "application/json")
    public List<UnidadeAdministrativaGetAllResponse> listarUnidadesAdministrativas() {
        return fachada.listarUnidadesAdministrativas().stream()
                .map(unidadeAdministrativa -> new UnidadeAdministrativaGetAllResponse(unidadeAdministrativa, modelMapper))
                .toList();
    }

    @GetMapping(value = "/montarArvore", produces  = "application/json")
    public ResponseEntity<List<UnidadeAdministrativaResponse>> montarArvore() {
        List<UnidadeAdministrativaResponse> response = fachada.montarArvore().stream()
                .map(unidade -> new UnidadeAdministrativaResponse(unidade, modelMapper))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/listarUnidadesFilhas/{id}", produces = "application/json")
    public ResponseEntity<List<UnidadeAdministrativaGetResponse>> listarUnidadesFilhas(@PathVariable Long id) {
        List<UnidadeAdministrativaGetResponse> response = fachada.listarUnidadesFilhas(id).stream()
                .map(unidade -> new UnidadeAdministrativaGetResponse(unidade, modelMapper))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deletarUnidadeAdministrativa(@PathVariable Long id) throws UnidadeAdministrativaNotFoundException {
        fachada.deletarUnidadeAdministrativa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{unidadeId}/gestores/{usuarioId}")
    public ResponseEntity<UnidadeAdministrativaGetResponse> adicionarGestor(@PathVariable Long unidadeId, @PathVariable Long usuarioId) {
        UnidadeAdministrativa response = fachada.adicionarGestor(unidadeId, usuarioId);
        return ResponseEntity.ok(new UnidadeAdministrativaGetResponse(response, modelMapper));
    }

    @DeleteMapping("/{unidadeId}/gestores")
    public ResponseEntity<UnidadeAdministrativaGetResponse> removerGestor(@PathVariable Long unidadeId) {
        UnidadeAdministrativa response = fachada.removerGestor(unidadeId);
        return ResponseEntity.ok(new UnidadeAdministrativaGetResponse(response, modelMapper));
    }

    @PostMapping("/{unidadeId}/tecnicos/{usuarioId}")
    public ResponseEntity<UnidadeAdministrativaGetResponse> adicionarTecnico(@PathVariable Long unidadeId, @PathVariable Long usuarioId) {
        UnidadeAdministrativa response = fachada.adicionarTecnico(unidadeId, usuarioId);
        return ResponseEntity.ok(new UnidadeAdministrativaGetResponse(response, modelMapper));
    }

    @DeleteMapping("/{unidadeId}/tecnicos/{usuarioId}")
    public ResponseEntity<UnidadeAdministrativaGetResponse> removerTecnico(@PathVariable Long unidadeId, @PathVariable Long usuarioId) {
        UnidadeAdministrativa response = fachada.removerTecnico(unidadeId, usuarioId);
        return ResponseEntity.ok(new UnidadeAdministrativaGetResponse(response, modelMapper));
    }



}
