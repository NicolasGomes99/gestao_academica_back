package br.edu.ufape.sguAuthService.comunicacao.controllers;





import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioPatchRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioRequest;
import br.edu.ufape.sguAuthService.comunicacao.dto.usuario.UsuarioResponse;
import br.edu.ufape.sguAuthService.exceptions.notFoundExceptions.UsuarioNotFoundException;
import br.edu.ufape.sguAuthService.fachada.Fachada;

import br.edu.ufape.sguAuthService.models.Usuario;
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

import java.util.UUID;

@RestController@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {
    private final Fachada fachada;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioRequest.convertToEntity(usuarioRequest, modelMapper);
        Usuario response = fachada.salvarUsuario(usuario, usuarioRequest.getTipoEtniaId(), usuarioRequest.getSenha());
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable UUID id) throws UsuarioNotFoundException {
        Usuario response = fachada.buscarUsuario(id);
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public Page<UsuarioResponse> listarUsuarios(
            @QuerydslPredicate(root = Usuario.class) Predicate predicate,
            @PageableDefault(value = 2)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return fachada.listarUsuarios(predicate, pageable).map(usuario -> new UsuarioResponse(usuario, modelMapper));

    }

    @GetMapping("/current")
    public ResponseEntity<UsuarioResponse> buscarUsuarioAtual() throws UsuarioNotFoundException {
        Usuario response = fachada.buscarUsuarioAtual();
        return new ResponseEntity<>(new UsuarioResponse(response, modelMapper), HttpStatus.OK);
    }


    @PatchMapping
    public ResponseEntity<UsuarioResponse> atualizar(@RequestBody UsuarioPatchRequest usuario)
            throws UsuarioNotFoundException {
        Usuario atualizado = fachada.editarUsuario(usuario);
        return ResponseEntity.ok(new UsuarioResponse(atualizado, modelMapper));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar() throws UsuarioNotFoundException {
        fachada.deletarUsuario();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) throws UsuarioNotFoundException {
        fachada.deletarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
