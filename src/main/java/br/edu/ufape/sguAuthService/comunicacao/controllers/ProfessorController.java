package br.edu.ufape.sguAuthService.comunicacao.controllers;


import br.edu.ufape.sguAuthService.comunicacao.dto.professor.ProfessorResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.ProfessorNotFoundException;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;
import br.edu.ufape.sguAuthService.models.Usuario;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/professor")
public class ProfessorController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    ResponseEntity<ProfessorResponse> buscarProfessor(@PathVariable UUID id) throws ProfessorNotFoundException, UsuarioNotFoundException {
        Usuario response = fachada.buscarProfessor(id);
        return new ResponseEntity<>(new ProfessorResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GESTOR')")
    @GetMapping
    public Page<ProfessorResponse> listarProfessores(@QuerydslPredicate(root = Usuario.class) Predicate predicate,
                                                     @PageableDefault(value = 2)
                                                     @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                                     Pageable pageable) {
        return fachada.listarProfessores(predicate, pageable).map(usuario -> new ProfessorResponse(usuario, modelMapper));
    }
}
