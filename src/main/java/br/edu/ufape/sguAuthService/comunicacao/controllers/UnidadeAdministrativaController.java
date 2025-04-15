package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa.*;
import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unidade-administrativa") @RequiredArgsConstructor

public class UnidadeAdministrativaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;


    @PostMapping
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

    @GetMapping
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

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/gestores")
    public ResponseEntity<UsuarioResponse> alocarGestor(
            @RequestBody @Valid UnidadeAdministrativaAlocarUsuarioRequest request) {

        Usuario gestor = fachada.adicionarGestor(
                request.getUnidadeAdministrativaId(),
                request.getUsuarioId()
        );
        return ResponseEntity.ok(new UsuarioResponse(gestor, modelMapper));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/gestores")
    public ResponseEntity<UsuarioResponse> removerGestor(
            @RequestBody @Valid UnidadeAdministrativaAlocarUsuarioRequest request) {

        Usuario gestor = fachada.removerGestor(
                request.getUnidadeAdministrativaId(),
                request.getUsuarioId()
        );
        return ResponseEntity.ok(new UsuarioResponse(gestor, modelMapper));
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/tecnicos")
    public ResponseEntity<UsuarioResponse> alocarTecnico(
            @RequestBody @Valid UnidadeAdministrativaAlocarUsuarioRequest request) {

        Usuario tecnico = fachada.adicionarTecnico(
                request.getUnidadeAdministrativaId(),
                request.getUsuarioId()
        );
        return ResponseEntity.ok(new UsuarioResponse(tecnico, modelMapper));
    }

    @PreAuthorize("hasRole('GESTOR')")
    @DeleteMapping("/tecnicos")
    public ResponseEntity<UsuarioResponse> removerTecnico(
            @RequestBody @Valid UnidadeAdministrativaAlocarUsuarioRequest request) {

        Usuario tecnico = fachada.removerTecnico(
                request.getUnidadeAdministrativaId(),
                request.getUsuarioId()
        );
        return ResponseEntity.ok(new UsuarioResponse(tecnico, modelMapper));
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping(value = "/{id}/tecnicos")
    public ResponseEntity<GetTecnicosUnidadeResponse> listarTecnicosUnidade(@PathVariable Long id) throws UnidadeAdministrativaNotFoundException {
        UnidadeAdministrativa response = fachada.listarTecnicosUnidade(id);
        return new ResponseEntity<>(new GetTecnicosUnidadeResponse(response, modelMapper), HttpStatus.OK);
    }
}
