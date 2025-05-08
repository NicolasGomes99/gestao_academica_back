package br.edu.ufape.sguAuthService.dados;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;

public interface  TipoUnidadeAdministrativaRepository extends JpaRepository<TipoUnidadeAdministrativa, Long> {
   TipoUnidadeAdministrativa findByNome(String nome);
}
