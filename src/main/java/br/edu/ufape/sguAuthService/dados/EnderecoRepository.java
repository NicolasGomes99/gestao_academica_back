package br.edu.ufape.sguAuthService.dados;

import br.edu.ufape.sguAuthService.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}