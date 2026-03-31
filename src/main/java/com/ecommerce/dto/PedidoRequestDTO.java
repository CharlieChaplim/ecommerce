package com.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDTO(
    @NotNull(message = "ID do cliente é obrigatório")
    Long clienteId,
    
    @NotNull(message = "ID do endereço é obrigatório")
    Long enderecoId,
    
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    @Valid
    List<ItemPedidoRequestDTO> itens
) {}
