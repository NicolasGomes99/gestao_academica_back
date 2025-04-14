package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.funcionario.FuncionarioResponse;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/funcionario")
public class FuncionarioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/current")
    public ResponseEntity<FuncionarioResponse> getCurrentFuncionario() {
        Usuario response = fachada.buscarUsuarioAtual();
        return new ResponseEntity<>(new FuncionarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarFuncionarioPorId(@PathVariable UUID id) {
        Usuario response = fachada.buscarUsuario(id);
        return new ResponseEntity<>(new FuncionarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PostMapping("/batch")
    List<FuncionarioResponse> listarFuncionariosEmBatch(@RequestBody List<UUID> ids) {
        return fachada.listarUsuariosEmBatch(ids).stream().map(usuario -> new FuncionarioResponse(usuario, modelMapper)).toList();
    }

}
