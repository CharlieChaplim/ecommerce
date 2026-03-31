package com.ecommerce.dto;

import com.ecommerce.model.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoStatusPedidoDTO(
    @NotNull(message = "Status é obrigatório")
    PedidoStatus status
) {}
