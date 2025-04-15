package br.edu.ufape.sguAuthService.seeds;

import br.edu.ufape.sguAuthService.dados.TipoUnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TipoUnidadeAdministrativaSeeder {

    private final TipoUnidadeAdministrativaRepository tipoRepo;

    @PostConstruct
    public void seedTipos() {
        List<String> tiposNecessarios = List.of(
                "REITORIA",
                "PROREITORIA",
                "DIRETORIA",
                "COORDENADORIA",
                "SETOR"
        );

        for (String nome : tiposNecessarios) {
            tipoRepo.findByNomeEqualsIgnoreCase(nome)
                    .orElseGet(() -> {
                        System.out.println("Criando tipo: " + nome);
                        return tipoRepo.save(new TipoUnidadeAdministrativa(null, nome));
                    });
        }

        System.out.println("Tipos de unidade administrativa populados com sucesso");
    }
}
