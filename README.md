# API de Vendas

API REST para cadastrar, listar, consultar, atualizar, filtrar e excluir vendas. Desenvolvida com Java e Spring Boot, utiliza PostgreSQL para armazenar os dados e DTOs para receber e devolver informações em JSON.

## Tecnologias

- Java 17
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA e Hibernate
- PostgreSQL
- Lombok
- Maven

## Como o projeto funciona

Uma requisição enviada pelo Insomnia chega ao controller. Ele chama a service, que executa as regras de negócio e utiliza o repository para acessar o banco de dados.

```text
Cliente (Insomnia)
    → Controller
    → Service
    → Repository
    → PostgreSQL
```

Na volta, a service transforma a entidade em um DTO de resposta, e o controller devolve esse objeto como JSON.

### Organização dos arquivos

Os pacotes ficam em `src/main/java/br/com/joaocastro/vendas`:

```text
vendas/
├── VendasApplication.java
├── controller/
│   └── VendasController.java
├── dto/
│   ├── VendasRequest.java
│   └── VendasResponse.java
├── infra/
│   ├── entity/
│   │   └── Vendas.java
│   └── repository/
│       └── VendasRepository.java
└── service/
    └── VendasService.java
```

- **Controller:** define as URLs e os métodos HTTP da API e recebe os dados das requisições.
- **Service:** verifica duplicidade no cadastro, busca vendas existentes e realiza as conversões manuais entre DTOs e entidade.
- **Repository:** estende `JpaRepository<Vendas, Long>`, fornecendo operações de persistência e consultas por produto e data.
- **Entity:** representa a tabela `vendas` no banco.
- **DTOs:** definem os dados de entrada e saída da API. Foram implementados como `record`, sem uma classe mapper.

### Dados de uma venda

- `id`: identificador `Long`, gerado automaticamente pelo banco.
- `product`: nome do produto (`String`).
- `quantity`: quantidade (`Integer`).
- `price`: preço (`BigDecimal`), para representar valores decimais com precisão.
- `saleDate`: data da venda (`LocalDate`), sem horário, no formato `AAAA-MM-DD`. No banco, a coluna se chama `sale_date`.

`VendasRequest` contém os quatro campos de entrada, sem o ID. `VendasResponse` contém esses mesmos campos e o ID gerado. O repository trabalha com a entidade `Vendas`.

## Como executar

### 1. Preparar o ambiente

Instale Java 17, Maven e PostgreSQL. Confirme que o PostgreSQL está em execução e crie o banco usando um cliente SQL conectado ao servidor:

```sql
CREATE DATABASE vendas;
```

### 2. Configurar a conexão

No arquivo `src/main/resources/application.properties`, ajuste as credenciais para seu ambiente:

```properties
spring.application.name=vendas
spring.datasource.url=jdbc:postgresql://localhost:5432/vendas
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Com `ddl-auto=update`, o Hibernate tenta criar ou atualizar a estrutura das tabelas ao iniciar a aplicação. O banco `vendas` precisa existir antes da inicialização.

### 3. Iniciar a aplicação

Na raiz do projeto, onde está o `pom.xml`, execute:

```bash
mvn spring-boot:run
```

Também é possível executar a classe `VendasApplication` pelo IntelliJ. A URL base padrão é:

```text
http://localhost:8080/vendas
```

## Endpoints e exemplos no Insomnia

Para enviar dados, selecione **Body → JSON** e utilize `Content-Type: application/json`.

### Cadastrar uma venda

**POST** `http://localhost:8080/vendas`

```json
{
  "product": "Notebook",
  "quantity": 3,
  "price": 3500.90,
  "saleDate": "2026-10-28"
}
```

Não envie o ID no cadastro. A implementação atual retorna **200 OK** com a venda salva:

```json
{
  "id": 1,
  "product": "Notebook",
  "quantity": 3,
  "price": 3500.90,
  "saleDate": "2026-10-28"
}
```

O ID é apenas ilustrativo: o banco pode gerar outro número, e a sequência não precisa ser contínua.

### Listar todas as vendas

**GET** `http://localhost:8080/vendas`

Não utiliza body. Retorna **200 OK** com uma lista de vendas. Se não houver registros, retorna `[]`.

### Buscar uma venda por ID

**GET** `http://localhost:8080/vendas/1`

Não utiliza body. Retorna **200 OK** com a venda ou **404 Not Found** se o ID não existir.

### Atualizar uma venda

**PUT** `http://localhost:8080/vendas/1`

Envie todos os campos no JSON, pois a atualização substitui os quatro valores:

```json
{
  "product": "Notebook",
  "quantity": 2,
  "price": 3200.00,
  "saleDate": "2026-10-28"
}
```

O ID vem da URL. Retorna **200 OK** com a venda atualizada ou **404 Not Found** se ela não existir.

### Filtrar por data e produto

**GET** `http://localhost:8080/vendas/filtrar?data=2026-10-28&produto=Notebook`

Não utiliza body. Os parâmetros `data` e `produto` são obrigatórios e aplicados juntos. A consulta utiliza igualdade no nome do produto, sem busca por trechos. Retorna **200 OK** com uma lista, que pode estar vazia.

No Insomnia, também é possível preencher `data` e `produto` na aba **Params**.

### Excluir uma venda

**DELETE** `http://localhost:8080/vendas/1`

Não utiliza body. Retorna **204 No Content**, sem corpo, quando a venda é excluída, ou **404 Not Found** se o ID não existir.

## Regras e comportamento atual

- O cadastro verifica se já existe uma venda com produto, quantidade, preço e data iguais. Nesse caso, retorna **409 Conflict**.
- Essa verificação está na service e não constitui uma restrição de unicidade no banco. Requisições simultâneas podem passar pela consulta antes de uma delas salvar.
- A atualização ainda não faz a mesma verificação de duplicidade.
- Os DTOs ainda não possuem validações de campos obrigatórios, quantidade positiva ou preço positivo.
- O projeto ainda não implementa autenticação e autorização.

## Compilação e testes

Para compilar:

```bash
mvn compile
```

Para executar os testes existentes:

```bash
mvn test
```

Testes que carregam o contexto da aplicação podem precisar do PostgreSQL disponível e das credenciais configuradas.
