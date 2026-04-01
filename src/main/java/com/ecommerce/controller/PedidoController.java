package com.ecommerce.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.AtualizacaoStatusPedidoDTO;
import com.ecommerce.dto.PageResponseDTO;
import com.ecommerce.dto.PedidoRequestDTO;
import com.ecommerce.dto.PedidoResponseDTO;
import com.ecommerce.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO dto) {
        return new ResponseEntity<>(pedidoService.criar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<PedidoResponseDTO>> listar(
            @RequestParam(required = false) Long clienteId,
            Pageable pageable) {
        return ResponseEntity.ok(PageResponseDTO.fromPage(pedidoService.listar(clienteId, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizacaoStatusPedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
