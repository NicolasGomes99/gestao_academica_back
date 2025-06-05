package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa.TipoUnidadeAdministrativaRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.tipoUnidadeAdministrativa.TipoUnidadeAdministrativaResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoUnidadeAdministrativaNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tipo-unidade-administrativa")
public class TipoUnidadeAdministrativaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<TipoUnidadeAdministrativaResponse> salvar(@Valid @RequestBody TipoUnidadeAdministrativaRequest tipoUnidadeAdministrativa){
        TipoUnidadeAdministrativa response = fachada.salvarTipo(tipoUnidadeAdministrativa.convertToEntity(tipoUnidadeAdministrativa, modelMapper));
        return new ResponseEntity<>(new TipoUnidadeAdministrativaResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<TipoUnidadeAdministrativaResponse> editar(@PathVariable Long id, @Valid @RequestBody TipoUnidadeAdministrativaRequest tipoUnidadeAdministrativa) throws TipoUnidadeAdministrativaNotFoundException {
        TipoUnidadeAdministrativa response = fachada.editarTipo(id, tipoUnidadeAdministrativa.convertToEntity(tipoUnidadeAdministrativa, modelMapper));
        return new ResponseEntity<>(new TipoUnidadeAdministrativaResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUnidadeAdministrativaResponse> buscar(@PathVariable Long id) throws TipoUnidadeAdministrativaNotFoundException {
        TipoUnidadeAdministrativa response = fachada.buscarTipo(id);
        return new ResponseEntity<>(new TipoUnidadeAdministrativaResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<TipoUnidadeAdministrativaResponse>> listar(@PageableDefault(sort = "id") Pageable pageable) {
        Page<TipoUnidadeAdministrativa> tipos = fachada.listarTipos(pageable);
        Page<TipoUnidadeAdministrativaResponse> response = tipos.map(tipo -> new TipoUnidadeAdministrativaResponse(tipo, modelMapper));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws TipoUnidadeAdministrativaNotFoundException {
        fachada.deletarTipo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}