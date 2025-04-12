package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.funcionario.FuncionarioResponse;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/funcionario")
public class FuncionarioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/current")
    public ResponseEntity<FuncionarioResponse> getCurrentFuncionario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        Usuario response = fachada.buscarUsuarioPorKcId(principal.getSubject());
        return new ResponseEntity<>(new FuncionarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FuncionarioResponse> buscarFuncionarioPorId(@PathVariable String userId) {
        Usuario response = fachada.buscarUsuarioPorKcId(userId);
        return new ResponseEntity<>(new FuncionarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping("/batch")
    List<FuncionarioResponse> listarFuncionariosEmBatch(@RequestBody List<String> kcIds) {
        return fachada.listarUsuariosEmBatch(kcIds).stream().map(usuario -> new FuncionarioResponse(usuario, modelMapper)).toList();
    }

}
