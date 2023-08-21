package br.com.rponte.rinhadev.pessoas;

import br.com.rponte.rinhadev.base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContagemDePessoasControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private PessoaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("deve contabilizar numero de pessoas cadastradas")
    public void t1() throws Exception {
        // scenario
        List.of(
                new Pessoa("a", "aa", TODAY.minusYears(1)),
                new Pessoa("b", "bb", TODAY.minusYears(2)),
                new Pessoa("c", "cc", TODAY.minusYears(3))
        ).forEach(pessoa -> {
            repository.save(pessoa);
        });

        // action (and validation)
        mockMvc.perform(get("/contagem-pessoas")
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"))
        ;
    }

    @Test
    @DisplayName("deve contabilizar numero de pessoas cadastradas quando não houver nenhuma pessoa cadastrada")
    public void t2() throws Exception {
        // action (and validation)
        mockMvc.perform(get("/contagem-pessoas")
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"))
        ;
    }
}