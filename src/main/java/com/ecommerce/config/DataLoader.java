package com.ecommerce.config;

import com.ecommerce.model.Cliente;
import com.ecommerce.model.Endereco;
import com.ecommerce.model.Produto;
import com.ecommerce.repository.ClienteRepository;
import com.ecommerce.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public DataLoader(ClienteRepository clienteRepository, ProdutoRepository produtoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() == 0) {
            Cliente cliente = new Cliente(null, "João Silva", "joao@email.com");
            Endereco endereco = new Endereco(null, "Rua A, 123", "São Paulo", "01001-000", cliente);
            cliente.addEndereco(endereco);
            clienteRepository.save(cliente);

            Produto p1 = new Produto(null, "Notebook", new BigDecimal("4500.00"), 10);
            Produto p2 = new Produto(null, "Mouse", new BigDecimal("150.00"), 50);
            Produto p3 = new Produto(null, "Teclado", new BigDecimal("250.00"), 30);
            
            produtoRepository.saveAll(List.of(p1, p2, p3));
        }
    }
}
