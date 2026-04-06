package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "cliente.enderecos", "endereco", "itens", "itens.produto"})
    Optional<Pedido> findById(Long id);

    @EntityGraph(attributePaths = {"cliente", "cliente.enderecos", "endereco", "itens", "itens.produto"})
    Page<Pedido> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "cliente.enderecos", "endereco", "itens", "itens.produto"})
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);
}
