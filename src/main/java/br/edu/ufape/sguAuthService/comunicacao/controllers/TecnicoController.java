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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    TecnicoResponse buscarTecnico(@PathVariable Long id) throws TecnicoNotFoundException, UsuarioNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        return new TecnicoResponse(fachada.buscarTecnico(id, principal.getSubject()), modelMapper);
    }

    @GetMapping("/buscar/{kcId}")
    TecnicoResponse buscarTecnicoPorKcId(@PathVariable String kcId) throws TecnicoNotFoundException, UsuarioNotFoundException {
        return new TecnicoResponse(fachada.buscarTecnicoPorKcId(kcId), modelMapper);
    }

    @GetMapping("/current")
    ResponseEntity<TecnicoResponse> buscarTecnicoAtual() throws TecnicoNotFoundException, UsuarioNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        Usuario response = fachada.buscarTecnicoPorKcId(principal.getSubject());
        return new ResponseEntity<>(new TecnicoResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping("/batch")
    List<TecnicoResponse> listarTecnicosEmBatch(@RequestBody List<String> kcIds) {
        return fachada.listarUsuariosEmBatch(kcIds).stream().map(usuario -> new TecnicoResponse(usuario, modelMapper)).toList();
    }
}
