package br.com.rponte.rinhadev.pessoas;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    @Override
    @Query( """
            select p
              from Pessoa p
              left join fetch p.stack s
              where p.id = :id
            """)
    public Optional<Pessoa> findById(UUID id);

    public boolean existsByApelido(String apelido);

    @Query( """
            select p 
              from Pessoa p
             where p.apelido like %:texto%
                or p.nome like %:texto%
                or p.stack like %:texto%
          order by p.apelido
            """)
    public List<Pessoa> findByApelidoOrNomeOrStack(String texto, Pageable pageable);

    /**
     * Retorna as 50 primeiras pessoas encontradas
     */
    public default List<Pessoa> findTop50ByApelidoOrNomeOrStack(String texto) {
        return findByApelidoOrNomeOrStack(texto, PageRequest.ofSize(50));
    }

}
