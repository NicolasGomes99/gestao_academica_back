package br.edu.ufape.sguAuthService.comunicacao.controllers;



import br.edu.ufape.sguAuthService.comunicacao.dto.gestor.GestorResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.unidadeAdministrativa.UnidadeAdministrativaResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.GestorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController @RequiredArgsConstructor
@RequestMapping("/gestor")
public class GestorController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    ResponseEntity<GestorResponse> buscarGestor(@PathVariable UUID id) throws GestorNotFoundException, UsuarioNotFoundException {
        Usuario response = fachada.buscarGestor(id);
        return new ResponseEntity<>(new GestorResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    List<GestorResponse> listarGestores() {
        return fachada.listarGestores().stream().map(usuario -> new GestorResponse(usuario, modelMapper)).toList();
    }

}
