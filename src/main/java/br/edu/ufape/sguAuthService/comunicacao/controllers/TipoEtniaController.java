package br.edu.ufape.sguAuthService.comunicacao.controllers;

import br.edu.ufape.sguAuthService.comunicacao.dto.tipoEtnia.TipoEtniaRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.tipoEtnia.TipoEtniaResponse;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.TipoEtnia;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tipoEtnia")
@RequiredArgsConstructor
public class TipoEtniaController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> buscarTipoEtnia(@PathVariable Long id) {
        TipoEtnia tipoEtnia = fachada.buscarTipoEtnia(id);
        if (tipoEtnia == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtnia, modelMapper), HttpStatus.OK);
    }

    @GetMapping
    public List<TipoEtniaResponse> listarTiposEtnia() {
        return fachada.listarTiposEtnia().stream()
                .map(tipoEtnia -> new TipoEtniaResponse(tipoEtnia, modelMapper))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TipoEtniaResponse> criarTipoEtnia(@RequestBody TipoEtniaRequest tipoEtniaRequest) {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia novoTipoEtnia = fachada.salvarTipoEtnia(tipoEtnia);
        return new ResponseEntity<>(new TipoEtniaResponse(novoTipoEtnia, modelMapper), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoEtniaResponse> atualizarTipoEtnia(@PathVariable Long id, @RequestBody TipoEtniaRequest tipoEtniaRequest) {
        TipoEtnia tipoEtnia = tipoEtniaRequest.convertToEntity(tipoEtniaRequest, modelMapper);
        TipoEtnia tipoEtniaAtualizado = fachada.atualizarTipoEtnia(id, tipoEtnia);
        if (tipoEtniaAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TipoEtniaResponse(tipoEtniaAtualizado, modelMapper), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoEtnia(@PathVariable Long id) {
        fachada.deletarTipoEtnia(id);
        return ResponseEntity.noContent().build();
    }
}