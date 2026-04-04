package com.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.exception.BusinessException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cliente;
import com.ecommerce.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }
        // Configura bidirecionalidade dos endereços
        if (cliente.getEnderecos() != null) {
            cliente.getEnderecos().forEach(e -> e.setCliente(cliente));
        }
        return clienteRepository.saveAndFlush(cliente);
    }

    @Transactional(readOnly = true)
    public Page<Cliente> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return findById(id);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = findById(id);
        
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) && clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        
        // Atualiza endereços se fornecidos
        if (clienteAtualizado.getEnderecos() != null) {
            cliente.getEnderecos().clear();
            clienteRepository.saveAndFlush(cliente);
            clienteAtualizado.getEnderecos().forEach(e -> {
                cliente.addEndereco(e);
            });
        }
        
        return clienteRepository.saveAndFlush(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = findById(id);
        clienteRepository.delete(cliente);
    }

    protected Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }
}
