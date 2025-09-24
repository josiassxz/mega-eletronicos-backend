
package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCpf(String cpf);
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT c FROM Cliente c WHERE c.cidade = :cidade")
    List<Cliente> findByCidade(@Param("cidade") String cidade);
    
    @Query("SELECT c FROM Cliente c WHERE c.estado = :estado")
    List<Cliente> findByEstado(@Param("estado") String estado);
}
