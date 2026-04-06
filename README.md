# E-commerce API

Este é um projeto de uma API de e-commerce desenvolvida com Spring Boot. A API permite gerenciar clientes, produtos e pedidos, seguindo as melhores práticas de desenvolvimento, como a separação de responsabilidades entre as camadas de Controller e Service.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **H2 Database** (para desenvolvimento e testes)
- **Bean Validation** (para validação de dados)

## Funcionalidades

### Clientes
- Cadastro de novos clientes com múltiplos endereços.
- Listagem paginada de clientes.
- Busca de cliente por ID.
- Atualização de dados do cliente.
- Exclusão de clientes.

### Produtos
- Cadastro de produtos com controle de estoque.
- Listagem paginada de produtos.
- Busca de produto por ID.
- Atualização de dados do produto.
- Exclusão de produtos.

### Pedidos
- Criação de pedidos com baixa automática de estoque.
- Listagem de pedidos (geral ou por cliente).
- Busca de pedido por ID.
- Atualização do status do pedido (CRIADO, PAGO, ENVIADO, CANCELADO).
- Cancelamento de pedidos com devolução de estoque.
- O cálculo do valor total é realizado no servidor, garantindo a integridade dos dados.

## Regras de Negócio Implementadas

1.  **Camada de Serviço**: Toda a lógica de negócio está concentrada na camada de Service. Os Controllers são responsáveis apenas por receber as requisições e delegar para os serviços.
2.  **Repositórios**: Os repositórios são injetados apenas nos serviços, nunca diretamente nos controllers.
3.  **Relacionamentos**: Os relacionamentos entre as entidades (Cliente, Endereço, Produto, Pedido e ItemPedido) foram definidos para garantir a consistência dos dados.
4.  **Controle de Estoque**: A criação de um pedido verifica a disponibilidade em estoque e realiza a baixa. O cancelamento devolve os itens ao estoque.
5.  **Cálculo de Total**: O total do pedido é calculado no servidor somando o preço unitário dos produtos no momento da compra multiplicado pela quantidade, prevenindo manipulações externas.

## Como Executar

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.6 ou superior

### Passos
1. Clone o repositório.
2. Navegue até a pasta do projeto.
3. Execute o comando:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API estará disponível em `http://localhost:8080`.

## Endpoints Principais

- `GET /clientes`: Lista todos os clientes (paginado).
- `POST /clientes`: Cria um novo cliente.
- `GET /produtos`: Lista todos os produtos (paginado).
- `POST /produtos`: Cria um novo produto.
- `GET /pedidos`: Lista todos os pedidos.
- `POST /pedidos`: Cria um novo pedido.
- `PATCH /pedidos/{id}/status`: Atualiza o status de um pedido.
