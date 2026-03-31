package com.ecommerce.dto;

import com.ecommerce.model.PedidoStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime data,
    PedidoStatus status,
    BigDecimal total,
    Long clienteId,
    EnderecoResponseDTO endereco,
    List<ItemPedidoResponseDTO> itens
) {}
