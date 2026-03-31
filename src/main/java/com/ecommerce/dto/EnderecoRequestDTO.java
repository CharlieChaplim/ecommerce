package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequestDTO(
    @NotBlank(message = "Rua é obrigatória")
    String rua,
    
    @NotBlank(message = "Cidade é obrigatória")
    String cidade,
    
    @NotBlank(message = "CEP é obrigatório")
    String cep
) {}
