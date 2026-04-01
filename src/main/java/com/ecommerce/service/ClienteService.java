package com.ecommerce.service;

import com.ecommerce.dto.ClienteRequestDTO;
import com.ecommerce.dto.ClienteResponseDTO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ClienteMapper;
import com.ecommerce.model.Cliente;
import com.ecommerce.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email já cadastrado");
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(clienteMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = findById(id);
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = findById(id);
        
        if (!cliente.getEmail().equals(dto.email()) && clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        
        // Simples atualização de dados básicos para este exemplo
        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(salvo);
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
