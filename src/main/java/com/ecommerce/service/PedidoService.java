package com.ecommerce.service;

import com.ecommerce.dto.AtualizacaoStatusPedidoDTO;
import com.ecommerce.dto.PedidoRequestDTO;
import com.ecommerce.dto.PedidoResponseDTO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.PedidoMapper;
import com.ecommerce.model.*;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteService clienteService;
    private final PedidoMapper pedidoMapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         ClienteService clienteService,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteService = clienteService;
        this.pedidoMapper = pedidoMapper;
    }

    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO dto) {
        Cliente cliente = clienteService.findById(dto.clienteId());
        
        Endereco endereco = cliente.getEnderecos().stream()
                .filter(e -> e.getId().equals(dto.enderecoId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Endereço não pertence ao cliente informado"));

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus(PedidoStatus.CRIADO);
        pedido.setCliente(cliente);
        pedido.setEndereco(endereco);

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (var itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + itemDTO.produtoId()));

            if (produto.getEstoque() < itemDTO.quantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // Baixa estoque
            produto.setEstoque(produto.getEstoque() - itemDTO.quantidade());
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(produto.getPreco()); // Captura preço atual
            
            pedido.addItem(item);
            
            BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
            totalPedido = totalPedido.add(subtotal);
        }

        pedido.setTotal(totalPedido);
        Pedido salvo = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(salvo);
    }

    public Page<PedidoResponseDTO> listar(Long clienteId, Pageable pageable) {
        if (clienteId != null) {
            return pedidoRepository.findByClienteId(clienteId, pageable).map(pedidoMapper::toResponseDTO);
        }
        return pedidoRepository.findAll(pageable).map(pedidoMapper::toResponseDTO);
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        return pedidoMapper.toResponseDTO(findById(id));
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, AtualizacaoStatusPedidoDTO dto) {
        Pedido pedido = findById(id);
        PedidoStatus novoStatus = dto.status();
        validarTransicaoStatus(pedido.getStatus(), novoStatus);
        
        pedido.setStatus(novoStatus);
        return pedidoMapper.toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public void cancelar(Long id) {
        Pedido pedido = findById(id);
        
        if (pedido.getStatus() != PedidoStatus.CRIADO) {
            throw new BusinessException("Apenas pedidos com status CRIADO podem ser cancelados");
        }

        // Devolve estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(PedidoStatus.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void validarTransicaoStatus(PedidoStatus atual, PedidoStatus novo) {
        if (atual == PedidoStatus.CANCELADO) {
            throw new BusinessException("Pedido cancelado não pode ter o status alterado");
        }

        boolean valida = switch (atual) {
            case CRIADO -> novo == PedidoStatus.PAGO || novo == PedidoStatus.CANCELADO;
            case PAGO -> novo == PedidoStatus.ENVIADO;
            case ENVIADO -> false;
            case CANCELADO -> false;
        };

        if (!valida) {
            throw new BusinessException("Transição de status inválida: " + atual + " -> " + novo);
        }
    }

    private Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }
}
