package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.funcionario.FuncionarioResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.gestorUnidade.GestorUnidadeRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.gestorUnidade.GestorUnidadeResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa.*;
import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguAuthService.exceptions.unidadeAdministrativa.UnidadeAdministrativaNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.*;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public ResponseEntity<UnidadeAdministrativaGetResponse> editar(@PathVariable Long id, @Valid @RequestBody UnidadeAdministrativaPatchRequest unidadeAdministrativaPatchRequest) {
        try {
            UnidadeAdministrativa unidade = modelMapper.map(unidadeAdministrativaPatchRequest, UnidadeAdministrativa.class);
            UnidadeAdministrativa response = fachada.editarUnidadeAdministrativa(id, unidade);
            return new ResponseEntity<>(new UnidadeAdministrativaGetResponse(response, modelMapper), HttpStatus.OK);
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
    @PostMapping("/{id}/gestores")
    public ResponseEntity<GestorUnidadeResponse> alocarGestor(@PathVariable Long id, @RequestBody @Valid GestorUnidadeRequest request) {
        GestorUnidade gestor = request.convertToEntity(request, modelMapper);
        GestorUnidadeResponse gestorUnidade = new GestorUnidadeResponse(fachada.adicionarGestor(id, gestor, request.getUsuarioId()), modelMapper);
        return ResponseEntity.status(HttpStatus.CREATED).body(gestorUnidade);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}/gestores")
    public ResponseEntity<Void> removerGestor(@PathVariable Long id, @RequestBody Map<String, String> body) {
        UUID usuarioId = UUID.fromString(body.get("usuarioId"));
        fachada.removerGestor(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}/gestores")
    public Page<GestorUnidadeResponse> listarGestores(@PathVariable Long id, @QuerydslPredicate(root = GestorUnidade.class) Predicate predicate,
                                                      @PageableDefault(value = 2)
                                                      @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                      Pageable pageable)  {
        return fachada.listarGestoresPorUnidade(id, predicate, pageable)
                .map(g -> new GestorUnidadeResponse(g, modelMapper));
    }


    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/{id}/funcionarios")
    public ResponseEntity<UsuarioResponse> alocarFuncionarios( @PathVariable Long id, @RequestBody Map<String, String> body) {
        UUID usuarioId = UUID.fromString(body.get("usuarioId"));
        Usuario funcionario = fachada.adicionarFuncionario(id, usuarioId);
        return ResponseEntity.ok(new UsuarioResponse(funcionario, modelMapper));
    }

    @PreAuthorize("hasRole('GESTOR')")
    @DeleteMapping("/{id}/funcionarios")
    public ResponseEntity<UsuarioResponse> removerFuncionario(@PathVariable Long id, @RequestBody  Map<String, String> body) {
        UUID usuarioId = UUID.fromString(body.get("usuarioId"));
        fachada.removerFuncionario(id, usuarioId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/{id}/funcionarios")
    public Page<FuncionarioResponse> listarFuncionarios(@PathVariable Long id, @QuerydslPredicate(root = Funcionario.class) Predicate predicate,
                                                        @PageableDefault(value = 2)
                                                        @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                        Pageable pageable) {
        return fachada.listarFuncionariosPorUnidade(id, predicate, pageable)
                .map(funcionario -> new FuncionarioResponse(funcionario.getUsuario(), modelMapper));
    }

    @PreAuthorize("hasAnyRole('GESTOR')")
    @GetMapping("/gestor")
    public Page<UnidadeAdministrativaGetAllResponse> listarUnidadesDoGestorAtual(@QuerydslPredicate(root = UnidadeAdministrativa.class) Predicate predicate,
                                                                                 @PageableDefault(value = 2)
                                                                                 @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                                                 Pageable pageable) {
        return fachada.listarUnidadesDoGestorAtual(predicate, pageable)
                .map(u -> new UnidadeAdministrativaGetAllResponse(u, modelMapper));
    }

    @PreAuthorize("hasAnyRole('TECNICO', 'PROFESSOR')")
    @GetMapping("/funcionario")
    public Page<UnidadeAdministrativaGetAllResponse> listarUnidadesDoFuncionarioAtual(@QuerydslPredicate(root = UnidadeAdministrativa.class) Predicate predicate,
                                                                                      @PageableDefault(value = 2)
                                                                                      @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                                                      Pageable pageable) {
        return fachada.listarUnidadesDoFuncionarioAtual(predicate, pageable)
                .map(u -> new UnidadeAdministrativaGetAllResponse(u, modelMapper));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GESTOR')")
    @GetMapping("/gestor/{usuarioId}")
    public Page<UnidadeAdministrativaGetAllResponse> listarUnidadesDoGestorPorId(@PathVariable String usuarioId,
                                                                                 @QuerydslPredicate(root = UnidadeAdministrativa.class) Predicate predicate,
                                                                                 @PageableDefault(value = 2)
                                                                                     @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                                                     Pageable pageable) {
        UUID id = fachada.parseUUID(usuarioId, "ID do usuário inválido.");
        return fachada.listarUnidadesDoGestorPorId(id, predicate, pageable)
                .map(u -> new UnidadeAdministrativaGetAllResponse(u, modelMapper));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TECNICO', 'PROFESSOR')")
    @GetMapping("/funcionario/{usuarioId}")
    public Page<UnidadeAdministrativaGetAllResponse> listarUnidadesDoFuncionarioPorId(@PathVariable String usuarioId,
                                                                                      @QuerydslPredicate(root = UnidadeAdministrativa.class) Predicate predicate,
                                                                                      @PageableDefault(value = 2)
                                                                                          @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                                                          Pageable pageable) {
        UUID id = fachada.parseUUID(usuarioId, "ID do usuário inválido.");
        return fachada.listarUnidadesDoFuncionarioPorId(id, predicate, pageable)
                .map(u -> new UnidadeAdministrativaGetAllResponse(u, modelMapper));
    }
}
