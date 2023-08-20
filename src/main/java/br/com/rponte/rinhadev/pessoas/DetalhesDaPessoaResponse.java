package br.com.rponte.rinhadev.pessoas;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DetalhesDaPessoaResponse(UUID id, String apelido, String nome, LocalDate nascimento,
                                       List<String> stack) {
    public DetalhesDaPessoaResponse(Pessoa pessoa) {
        this(pessoa.getId(),
            pessoa.getApelido(),
            pessoa.getNome(),
            pessoa.getNascimento(),
            pessoa.getStack()
        );
    }
}
