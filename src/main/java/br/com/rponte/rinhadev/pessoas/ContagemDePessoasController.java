package br.com.rponte.rinhadev.pessoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContagemDePessoasController {

    @Autowired
    private PessoaRepository repository;

    @ResponseBody
    @GetMapping("/contagem-pessoas")
    public long contagem() {
        return repository.count();
    }

}
