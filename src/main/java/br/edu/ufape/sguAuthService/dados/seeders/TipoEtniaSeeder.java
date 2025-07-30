package br.edu.ufape.sguAuthService.dados.seeders;

import br.edu.ufape.sguAuthService.models.TipoEtnia;
import br.edu.ufape.sguAuthService.dados.TipoEtniaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TipoEtniaSeeder {

    private final TipoEtniaRepository tipoEtniaRepository;

    public TipoEtniaSeeder(TipoEtniaRepository tipoEtniaRepository) {
        this.tipoEtniaRepository = tipoEtniaRepository;
    }

    @EventListener
    public void seed(ApplicationReadyEvent event) {
        if (tipoEtniaRepository.count() == 0) {
            List<String> etnias = List.of("Branco", "Preto", "Pardo", "Indígena", "Amarelo");

            for (String etnia : etnias) {
                tipoEtniaRepository.findByTipoIgnoreCase(etnia)
                        .orElseGet(() -> {
                            TipoEtnia novoTipo = new TipoEtnia();
                            novoTipo.setTipo(etnia);
                            return tipoEtniaRepository.save(novoTipo);
                        });
            }

            System.out.println("Tipos de Etnia inicializados com sucesso!");
        } else {
            System.out.println("Seeder de TipoEtnia ignorado: tabela já contém registros.");
        }
    }
}

