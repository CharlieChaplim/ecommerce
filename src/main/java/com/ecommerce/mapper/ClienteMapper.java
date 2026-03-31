package com.ecommerce.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ecommerce.dto.ClienteRequestDTO;
import com.ecommerce.dto.ClienteResponseDTO;
import com.ecommerce.dto.EnderecoRequestDTO;
import com.ecommerce.dto.EnderecoResponseDTO;
import com.ecommerce.model.Cliente;
import com.ecommerce.model.Endereco;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        
        if (dto.enderecos() != null) {
            dto.enderecos().forEach(e -> {
                Endereco endereco = toEnderecoEntity(e);
                cliente.addEndereco(endereco);
            });
        }
        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        List<EnderecoResponseDTO> enderecos = cliente.getEnderecos().stream()
                .map(this::toEnderecoResponseDTO)
                .collect(Collectors.toList());
        
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                enderecos
        );
    }

    public Endereco toEnderecoEntity(EnderecoRequestDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.rua());
        endereco.setCidade(dto.cidade());
        endereco.setCep(dto.cep());
        return endereco;
    }

    public EnderecoResponseDTO toEnderecoResponseDTO(Endereco endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }
}
