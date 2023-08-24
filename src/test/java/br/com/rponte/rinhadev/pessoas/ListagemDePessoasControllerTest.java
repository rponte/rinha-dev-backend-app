package br.com.rponte.rinhadev.pessoas;

import br.com.rponte.rinhadev.base.SpringBootIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListagemDePessoasControllerTest extends SpringBootIntegrationTest {

    @Autowired
    private PessoaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new Pessoa("betão", "Alberto Souza", TODAY.minusYears(40))
                        .adicionaStack("java")
                        .adicionaStack("MySQL"),
                new Pessoa("Maraja e principe do java", "Rafael Ponte", TODAY.minusYears(39))
                        .adicionaStack("kotlin")
                        .adicionaStack("hibernate")
                        .adicionaStack("Postgresql"),
                new Pessoa("A PrInCeSa", "Marilia Ponte do Java", TODAY.minusYears(39))
                        .adicionaStack("go")
                        .adicionaStack("redis")
        ));
    }

    @Test
    @DisplayName("deve encontrar pessoas por apelido")
    public void t1() throws Exception {
        // action (and validation)
        String termo = "princ";
        mockMvc.perform(get("/pessoas?t={termo}", termo)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].apelido").value("A PrInCeSa"))
                .andExpect(jsonPath("$[1].apelido").value("Maraja e principe do java"))
        ;
    }

    @Test
    @DisplayName("deve encontrar pessoas por nome")
    public void t2() throws Exception {
        // action (and validation)
        String termo = "berto";
        mockMvc.perform(get("/pessoas?t={termo}", termo)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].apelido").value("betão"))
        ;
    }

    @Test
    @DisplayName("deve encontrar pessoas por stack")
    public void t3() throws Exception {
        // action (and validation)
        String termo = "sql";
        mockMvc.perform(get("/pessoas?t={termo}", termo)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].apelido").value("betão"))
                .andExpect(jsonPath("$[1].apelido").value("Maraja e principe do java"))
        ;
    }

    @Test
    @DisplayName("deve encontrar pessoas por apelido, nome e stack")
    public void t4() throws Exception {
        // action (and validation)
        String termo = "JAVA";
        mockMvc.perform(get("/pessoas?t={termo}", termo)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].apelido").value("A PrInCeSa"))
                .andExpect(jsonPath("$[1].apelido").value("betão"))
                .andExpect(jsonPath("$[2].apelido").value("Maraja e principe do java"))
        ;
    }

    @Test
    @DisplayName("não deve encontrar pessoas por apelido, nome e stack")
    public void t5() throws Exception {
        // action (and validation)
        String termo = "ruby";
        mockMvc.perform(get("/pessoas?t={termo}", termo)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

}