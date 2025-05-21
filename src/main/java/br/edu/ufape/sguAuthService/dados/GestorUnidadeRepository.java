package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.GestorUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GestorUnidadeRepository extends JpaRepository<GestorUnidade, Long> {

    List<GestorUnidade> findByGestorId(Long id);
}