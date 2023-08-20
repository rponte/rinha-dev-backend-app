package br.com.rponte.rinhadev.pessoas;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Pessoa {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, updatable = false, unique = true)
    private String apelido;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String nome;

    @Past
    @NotNull
    @Column(nullable = false)
    private LocalDate nascimento;

    @OrderColumn
    @ElementCollection
    private List<@NotBlank @Size(max = 32) String> stack = new ArrayList<>();

    public Pessoa(String apelido, String nome, LocalDate nascimento) {
        this.apelido = apelido;
        this.nome = nome;
        this.nascimento = nascimento;
    }

    public UUID getId() {
        return id;
    }
    public String getApelido() {
        return apelido;
    }
    public String getNome() {
        return nome;
    }
    public LocalDate getNascimento() {
        return nascimento;
    }
    public List<String> getStack() {
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(apelido, pessoa.apelido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apelido);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", apelido='" + apelido + '\'' +
                ", nome='" + nome + '\'' +
                ", nascimento=" + nascimento +
                ", stack=" + stack +
                '}';
    }

    /**
     * Adiciona nova stack a pessoa
     */
    public void adicionaStack(String novaStack) {
        Assert.hasLength(novaStack, "stack não pode ser nula ou vazia");
        Assert.isTrue(novaStack.length() <= 32, "stack deve ter máximo de 32 caracteres");

        this.stack.add(novaStack);
    }

}
