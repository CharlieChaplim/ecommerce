package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Produto;
import com.ecommerce.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Page<Produto> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return findById(id);
    }

    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = findById(id);
        produto.setNome(produtoAtualizado.getNome());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setEstoque(produtoAtualizado.getEstoque());
        return produtoRepository.save(produto);
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
