package com.ecommerce.service;

import com.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.dto.ProdutoResponseDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProdutoMapper;
import com.ecommerce.model.Produto;
import com.ecommerce.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        return produtoMapper.toResponseDTO(produtoRepository.save(produto));
    }

    public Page<ProdutoResponseDTO> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(produtoMapper::toResponseDTO);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        return produtoMapper.toResponseDTO(findById(id));
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = findById(id);
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        return produtoMapper.toResponseDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = findById(id);
        produtoRepository.delete(produto);
    }

    protected Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }
}
