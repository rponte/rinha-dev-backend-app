package br.com.rponte.rinhadev.pessoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class ListagemDePessoasController {

    @Autowired
    private PessoaRepository repository;

    @GetMapping("/pessoas")
    public ResponseEntity<?> lista(@NotBlank @RequestParam("t") String texto) {
        List<Pessoa> pessoas = repository.findTop50ByApelidoOrNomeOrStack(texto);
        return ResponseEntity.ok(
                pessoas.stream().map(DetalhesDaPessoaResponse::new).toList()
        );
    }
}
