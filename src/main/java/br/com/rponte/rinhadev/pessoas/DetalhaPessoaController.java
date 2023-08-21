package br.com.rponte.rinhadev.pessoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class DetalhaPessoaController {

    @Autowired
    private PessoaRepository repository;

    @Transactional(readOnly = true)
    @GetMapping("/pessoas/{id}")
    public ResponseEntity<?> buscaPorId(@NotNull @PathVariable("id") UUID id) {

        Pessoa pessoa = repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(NOT_FOUND, "pessoa não encontrada");
        });

        return ResponseEntity
                .ok(new DetalhesDaPessoaResponse(pessoa));
    }
}
