package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.GestorUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestorUnidadeRepository extends JpaRepository<GestorUnidade, Long> {
}