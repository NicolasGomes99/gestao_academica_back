package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.TipoEtnia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoEtniaRepository extends JpaRepository<TipoEtnia, Long> {
    Optional<TipoEtnia> findByTipoIgnoreCase(String tipo);
}

