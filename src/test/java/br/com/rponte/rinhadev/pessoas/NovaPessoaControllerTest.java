package br.com.rponte.rinhadev.pessoas;

import br.com.rponte.rinhadev.base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.zalando.problem.spring.common.MediaTypes;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NovaPessoaControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private PessoaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("deve criar uma nova pessoa")
    public void t1() throws Exception {
        // scenario
        NovaPessoaRequest request = new NovaPessoaRequest(
                "Maraja dos Legados",
                "Rafael Ponte",
                LocalDate.of(1984, 03, 07),
                List.of("java", "spring boot", "postgresql")
        );

        // action (and validation)
        mockMvc.perform(post("/pessoas")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/pessoas/*"))
        ;

        // validation
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("não deve criar pessoa quando parametros forem vazios")
    public void t2() throws Exception {
        // scenario
        NovaPessoaRequest request = new NovaPessoaRequest("", "", null, null);

        // action (and validation)
        mockMvc.perform(post("/pessoas")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(3)))
                .andExpect(jsonPath("$.violations", containsInAnyOrder(
                                violation("apelido", "must not be blank"),
                                violation("nome", "must not be blank"),
                                violation("nascimento", "must not be null")
                        )
                ))
        ;

        // validation
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("não deve criar pessoa quando parametros forem invalidos")
    public void t3() throws Exception {
        // scenario
        NovaPessoaRequest request = new NovaPessoaRequest(
                "a".repeat(33),
                "b".repeat(101),
                LocalDate.now().plusDays(1),
                List.of("ok1", "", "ok2")
        );

        // action (and validation)
        mockMvc.perform(post("/pessoas")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(4)))
                .andExpect(jsonPath("$.violations", containsInAnyOrder(
                                violation("apelido", "size must be between 0 and 32"),
                                violation("nome", "size must be between 0 and 100"),
                                violation("nascimento", "must be a past date"),
                                violation("stack[1]", "must not be blank")
                        )
                ))
        ;

        // validation
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("não deve criar pessoa quando outra pessoa com mesmo apelido já existe no sistema")
    public void t4() throws Exception {
        // scenario
        NovaPessoaRequest request = new NovaPessoaRequest(
                "Principe do Oceano",
                "Rafael Ponte",
                LocalDate.of(1984, 03, 07),
                List.of("java", "spring boot", "postgresql")
        );

        Pessoa existente = request.toModel();
        repository.save(existente);

        // action (and validation)
        mockMvc.perform(post("/pessoas")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.title", is("Unprocessable Entity")))
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.detail", is("pessoa com mesmo apelido existente no sistema")))
        ;

        // validation
        assertEquals(1, repository.count());
    }

}