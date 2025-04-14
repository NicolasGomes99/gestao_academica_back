package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.tecnico.TecnicoResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.TecnicoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tecnico")
public class TecnicoController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    List<TecnicoResponse> listarTecnicos() {
        return fachada.listarTecnicos().stream().map(usuario -> new TecnicoResponse(usuario, modelMapper)).toList();
    }

    @GetMapping("/{id}")
    TecnicoResponse buscarTecnico(@PathVariable UUID id) throws TecnicoNotFoundException, UsuarioNotFoundException {
        return new TecnicoResponse(fachada.buscarTecnico(id), modelMapper);
    }

    @GetMapping("/current")
    ResponseEntity<TecnicoResponse> buscarTecnicoAtual() throws TecnicoNotFoundException, UsuarioNotFoundException {
        Usuario response = fachada.buscarTecnicoAtual();
        return new ResponseEntity<>(new TecnicoResponse(response, modelMapper), HttpStatus.OK);
    }

}
