package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.aluno.AlunoRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.documento.DocumentoResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.gestor.GestorRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.professor.ProfessorRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.solicitacaoPerfil.SolicitacaoPerfilResponse;
import br.edu.ufape.sguAuthService.comunicacao.dto.solicitacaoPerfil.SolicitacaoPerfilRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.tecnico.TecnicoRequest;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.CursoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.SolicitacaoNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Aluno;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/solicitacao")
public class SolicitacaoPerfilController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/aluno", consumes = "multipart/form-data")
    public ResponseEntity<SolicitacaoPerfilResponse> solicitarPerfilAluno(@ModelAttribute AlunoRequest alunoRequest) throws CursoNotFoundException, UsuarioNotFoundException {
        Aluno aluno = alunoRequest.convertToEntity(modelMapper, fachada);
        return new ResponseEntity<>( new SolicitacaoPerfilResponse(fachada.solicitarPerfil(aluno,alunoRequest.getDocumentos()), modelMapper), HttpStatus.CREATED);
    }


    @PostMapping(value = "/professor", consumes = "multipart/form-data")
    public ResponseEntity<SolicitacaoPerfilResponse> solicitarPerfilProfessor(@ModelAttribute ProfessorRequest professorRequest) throws  UsuarioNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.solicitarPerfil(professorRequest.convertToEntity(modelMapper, fachada), professorRequest.getDocumentos()), modelMapper), HttpStatus.CREATED);
    }


    @PostMapping(value = "/tecnico", consumes = "multipart/form-data")
    public ResponseEntity<SolicitacaoPerfilResponse> solicitarPerfilTecnico(@ModelAttribute TecnicoRequest tecnicoRequest) throws  UsuarioNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.solicitarPerfil(tecnicoRequest.convertToEntity(modelMapper),tecnicoRequest.getDocumentos()), modelMapper), HttpStatus.CREATED);
    }

    @PostMapping(value = "/gestor", consumes = "multipart/form-data")
    public ResponseEntity<SolicitacaoPerfilResponse> solicitarPerfilGestor(@ModelAttribute GestorRequest gestorRequest) throws  UsuarioNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.solicitarPerfil(gestorRequest.convertToEntity(modelMapper),gestorRequest.getDocumentos()), modelMapper), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoPerfilResponse> aprovarSolicitacao(@PathVariable Long id, @RequestBody SolicitacaoPerfilRequest parecer) throws SolicitacaoNotFoundException, UsuarioNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.aceitarSolicitacao(id, parecer.convertToEntity(modelMapper)), modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<SolicitacaoPerfilResponse> rejeitarSolicitacao(@PathVariable Long id, @RequestBody SolicitacaoPerfilRequest parecer) throws SolicitacaoNotFoundException, UsuarioNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.rejeitarSolicitacao(id, parecer.convertToEntity(modelMapper)), modelMapper), HttpStatus.OK);
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoResponse>> baixarTodosDocumentos(@PathVariable Long id) throws IOException, SolicitacaoNotFoundException {
        return ResponseEntity.ok(fachada.listarDocumentosBase64(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoPerfilResponse> buscarSolicitacao(@PathVariable Long id) throws SolicitacaoNotFoundException {
        return new ResponseEntity<>(new SolicitacaoPerfilResponse(fachada.buscarSolicitacao(id), modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public List<SolicitacaoPerfilResponse> listarSolicitacoes() {
        return fachada.listarSolicitacoes().stream().map(solicitacao -> new SolicitacaoPerfilResponse(solicitacao, modelMapper)).toList();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/pendentes")
    public List<SolicitacaoPerfilResponse> listarSolicitacoesPendentes() {
        return fachada.listarSolicitacoesPendentes().stream().map(solicitacao -> new SolicitacaoPerfilResponse(solicitacao, modelMapper)).toList();
    }

    @GetMapping("/usuario")
    public List<SolicitacaoPerfilResponse> buscarSolicitacoesUsuario() {
        return fachada.buscarSolicitacoesUsuarioAtual().stream().map(solicitacao -> new SolicitacaoPerfilResponse(solicitacao, modelMapper)).toList();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}/usuario")
    public List<SolicitacaoPerfilResponse> buscarSolicitacoesPorId(@PathVariable UUID id) {
        return fachada.buscarSolicitacoesPorId(id).stream().map(solicitacao -> new SolicitacaoPerfilResponse(solicitacao, modelMapper)).toList();
    }
}
