package com.ecommerce.controller;

import com.ecommerce.model.Produto;
import com.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid Produto produto) {
        return new ResponseEntity<>(produtoService.salvar(produto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> listar(Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody @Valid Produto produto) {
        return ResponseEntity.ok(produtoService.atualizar(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
