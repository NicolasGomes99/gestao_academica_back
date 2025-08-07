package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.tipoEtnia.TipoEtniaRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.tipoEtnia.TipoEtniaResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TipoEtniaNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import com.querydsl.core.types.Predicate;
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

@RestController
@RequestMapping("/tipoEtnia")
@RequiredArgsConstructor
public class    TipoEtniaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> buscarTipoEtnia(@PathVariable Long id) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtnia = fachada.buscarTipoEtnia(id);
        if (tipoEtnia == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtnia, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public Page<TipoEtniaResponse> listarTiposEtnia(@QuerydslPredicate(root = TipoEtnia.class) Predicate predicate,
                                                    @PageableDefault(value = 2)
                                                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                    Pageable pageable) {
        return fachada.listarTiposEtnia(predicate, pageable)
                .map(tipoEtnia -> new TipoEtniaResponse(tipoEtnia, modelMapper));
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<TipoEtniaResponse> criarTipoEtnia(@RequestBody TipoEtniaRequest tipoEtniaRequest) {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia novoTipoEtnia = fachada.salvarTipoEtnia(tipoEtnia);
        return new ResponseEntity<>(new TipoEtniaResponse(novoTipoEtnia, modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'ADMINISTRADOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> atualizarTipoEtnia(@PathVariable Long id, @RequestBody TipoEtniaRequest tipoEtniaRequest) throws TipoEtniaNotFoundException {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia tipoEtniaAtualizado = fachada.atualizarTipoEtnia(id, tipoEtnia);
        if (tipoEtniaAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtniaAtualizado, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoEtnia(@PathVariable Long id) throws TipoEtniaNotFoundException {
        fachada.deletarTipoEtnia(id);
        return ResponseEntity.noContent().build();
    }
}