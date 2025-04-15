package br.edu.ufape.sguAuthService.seeds;

import br.edu.ufape.sguAuthService.dados.TipoUnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnidadeAdministrativaSeeder implements CommandLineRunner {

    private final UnidadeAdministrativaRepository unidadeRepo;
    private final TipoUnidadeAdministrativaRepository tipoRepo;

    @Override
    public void run(String... args) throws Exception {
        if (unidadeRepo.count() > 0) {
            System.out.println("Unidades administrativas já existem. Seeder ignorado.");
            return;
        }

        TipoUnidadeAdministrativa proReitoria = tipoRepo.findByNomeEqualsIgnoreCase("PROREITORIA")
                .orElseThrow(() -> new RuntimeException("Tipo 'PROREITORIA' não encontrado"));

        TipoUnidadeAdministrativa reitoria = tipoRepo.findByNomeEqualsIgnoreCase("REITORIA")
                .orElseThrow(() -> new RuntimeException("Tipo 'REITORIA' não encontrado"));

        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Administração", "PROAD", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Ensino e Graduação", "PREG", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Extensão e Cultura", "PREC", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Planejamento", "PROPLAN", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Pesquisa, Pós-Graduação e Inovação", "PRPPGI", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Pró-Reitoria de Gestão de Pessoas", "PROGEPE", proReitoria, null, null, null));
        unidadeRepo.save(new UnidadeAdministrativa(null, "Reitoria", "REIT", reitoria, null, null, null));

        System.out.println("Unidades administrativas criadas com sucesso");
    }
}
