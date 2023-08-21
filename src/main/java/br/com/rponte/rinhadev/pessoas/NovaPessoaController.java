package br.com.rponte.rinhadev.pessoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
public class NovaPessoaController {

    @Autowired
    private PessoaRepository repository;

    @Transactional
    @PostMapping("/pessoas")
    public ResponseEntity<?> cadastra(@Valid @RequestBody NovaPessoaRequest request, UriComponentsBuilder uriBuilder) {

        if (repository.existsByApelido(request.apelido())) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "pessoa com mesmo apelido existente no sistema");
        }

        Pessoa pessoa = request.toModel();
        repository.save(pessoa);

        return ResponseEntity
                .created(location(uriBuilder, pessoa))
                .build();
    }

    private static URI location(UriComponentsBuilder uriBuilder, Pessoa pessoa) {
        URI location = uriBuilder.path("/pessoas/{id}")
                .buildAndExpand(pessoa.getId())
                .toUri();
        return location;
    }

}
