package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.DadosBancarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadosBancariosRepository extends JpaRepository<DadosBancarios, Long> {
}