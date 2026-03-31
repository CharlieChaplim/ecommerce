package com.ecommerce.mapper;

import com.ecommerce.dto.EnderecoResponseDTO;
import com.ecommerce.dto.ItemPedidoResponseDTO;
import com.ecommerce.dto.PedidoResponseDTO;
import com.ecommerce.model.Endereco;
import com.ecommerce.model.ItemPedido;
import com.ecommerce.model.Pedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itens = pedido.getItens().stream()
                .map(this::toItemResponseDTO)
                .collect(Collectors.toList());

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getCliente().getId(),
                toEnderecoResponseDTO(pedido.getEndereco()),
                itens
        );
    }

    private ItemPedidoResponseDTO toItemResponseDTO(ItemPedido item) {
        BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
        return new ItemPedidoResponseDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                subtotal
        );
    }

    private EnderecoResponseDTO toEnderecoResponseDTO(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }
}
