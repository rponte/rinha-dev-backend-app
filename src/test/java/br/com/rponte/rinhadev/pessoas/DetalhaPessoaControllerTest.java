package br.com.rponte.rinhadev.pessoas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.spring.common.MediaTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class DetalhaPessoaControllerTest {

    private static LocalDate TODAY = LocalDate.now();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PessoaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("deve encontrar pessoa por ID")
    public void t1() throws Exception {
        // scenario
        List<Pessoa> existentes = repository.saveAll(List.of(
                new Pessoa("betão", "Alberto Souza", TODAY.minusYears(40))
                        .adicionaStack("java")
                        .adicionaStack("mysql"),
                new Pessoa("principe", "Rafael Ponte", TODAY.minusYears(39))
                        .adicionaStack("kotlin")
                        .adicionaStack("hibernate")
                        .adicionaStack("postgres")
        ));

        // action (and validation)
        for (Pessoa p : existentes) {
            mockMvc.perform(get("/pessoas/" + p.getId())
                            .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(p.getId().toString()))
                    .andExpect(jsonPath("apelido").value(p.getApelido()))
                    .andExpect(jsonPath("nome").value(p.getNome()))
                    .andExpect(jsonPath("nascimento").value(p.getNascimento().toString()))
                    .andExpect(jsonPath("$.stack", hasSize(p.getStack().size())))
                    .andExpect(jsonPath("$.stack", contains(p.getStack().toArray())))
            ;
        }
    }

    @Test
    @DisplayName("deve encontrar pessoa por ID quando ela não possuir stacks")
    public void t2() throws Exception {
        // scenario
        Pessoa p = repository.save(
                new Pessoa("marajá", "Rafael Ponte", TODAY.minusYears(39))
        );

        // action (and validation)
        mockMvc.perform(get("/pessoas/" + p.getId())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(p.getId().toString()))
                .andExpect(jsonPath("apelido").value(p.getApelido()))
                .andExpect(jsonPath("nome").value(p.getNome()))
                .andExpect(jsonPath("nascimento").value(p.getNascimento().toString()))
                .andExpect(jsonPath("$.stack").isEmpty())
        ;
    }

    @Test
    @DisplayName("não deve encontrar pessoa por ID quando ela não existir")
    public void t3() throws Exception {
        // scenario
        UUID pessoaNaoExistenteId = UUID.randomUUID();

        // action (and validation)
        mockMvc.perform(get("/pessoas/" + pessoaNaoExistenteId)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isNotFound())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("pessoa não encontrada")))
        ;
    }

}