package br.com.rponte.rinhadev.pessoas;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record NovaPessoaRequest(
        @NotBlank
        @Size(max = 32)
        String apelido,
        @NotBlank
        @Size(max = 100)
        String nome,
        @Past
        @NotNull
        LocalDate nascimento,
        List<@NotBlank @Size(max = 32) String> stack
) {

        public Pessoa toModel() {
                Pessoa pessoa = new Pessoa(apelido, nome, nascimento);
                this.stack.forEach(s -> {
                        pessoa.adicionaStack(s);
                });
                return pessoa;
        }
}
