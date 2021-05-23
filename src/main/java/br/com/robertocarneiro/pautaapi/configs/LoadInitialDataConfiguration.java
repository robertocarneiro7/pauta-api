package br.com.robertocarneiro.pautaapi.configs;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.repositories.AssociadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!performance-test")
public class LoadInitialDataConfiguration {

    @Bean
    public CommandLineRunner loadData(AssociadoRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {
                repository.save(Associado.builder().cpf("45547622036").build());
                repository.save(Associado.builder().cpf("21950795098").build());
                repository.save(Associado.builder().cpf("62566390061").build());
                repository.save(Associado.builder().cpf("41135086079").build());
                repository.save(Associado.builder().cpf("58907189080").build());
                repository.save(Associado.builder().cpf("00000000000").build());
            }
        };
    }
}
