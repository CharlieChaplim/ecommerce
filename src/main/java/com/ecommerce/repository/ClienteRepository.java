package com.ecommerce.repository;

import com.ecommerce.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @EntityGraph(attributePaths = {"enderecos", "pedidos", "pedidos.itens", "pedidos.itens.produto"})
    Optional<Cliente> findById(Long id);

    @EntityGraph(attributePaths = {"enderecos", "pedidos", "pedidos.itens", "pedidos.itens.produto"})
    Page<Cliente> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"enderecos", "pedidos", "pedidos.itens", "pedidos.itens.produto"})
    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);
}
