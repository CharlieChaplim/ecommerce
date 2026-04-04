package com.ecommerce.controller;

import com.ecommerce.model.Pedido;
import com.ecommerce.model.PedidoStatus;
import com.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody @Valid Pedido pedido) {
        return new ResponseEntity<>(pedidoService.criar(pedido), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Pedido>> listar(
            @RequestParam(required = false) Long clienteId,
            Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listar(clienteId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, PedidoStatus> statusUpdate) {
        PedidoStatus novoStatus = statusUpdate.get("status");
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
