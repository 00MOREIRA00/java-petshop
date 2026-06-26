# Projeto: Sistema de Petshop

## Objetivo

Criar uma aplicacao Java simples para estudar os principais conceitos da linguagem por meio do gerenciamento de produtos de um petshop.

## Estado atual

A aplicacao usa Java 17, Maven, JDBC e SQLite. Os produtos sao persistidos na tabela `produtos`, criada automaticamente no arquivo local `petshop.db`.

Ja estao implementadas as seguintes operacoes na camada de servico e repositorio:

- cadastrar produto;
- listar todos os produtos;
- buscar produto por ID;
- atualizar produto;
- remover produto por ID.

O `Main` ainda nao oferece um menu. Atualmente, ele cadastra um produto de exemplo e lista os produtos armazenados no SQLite.

## Modelo atual

Cada produto possui:

- `id`: identificador inteiro gerado pelo SQLite;
- `nome`: nome do produto;
- `preco`: preco do produto;
- `estoque`: quantidade armazenada.

Categoria, marca e descricao nao fazem parte do modelo atual.

## Estrutura

```text
src/main/java/
  br/com/petshop/
    Main.java
    model/
      Produto.java
    service/
      ProdutoService.java
    repository/
      ProdutoRepository.java
```

Responsabilidades:

- `Main`: ponto de entrada e demonstracao atual da aplicacao.
- `Produto`: representa os dados persistidos de um produto.
- `ProdutoService`: expoe as operacoes de produto e verifica a presenca do ID ao atualizar.
- `ProdutoRepository`: executa o CRUD no SQLite por meio de JDBC.

## Persistencia

O `ProdutoRepository` conecta-se a `jdbc:sqlite:petshop.db`. Na inicializacao, cria a tabela abaixo caso ela ainda nao exista:

```text
produtos
  id INTEGER PRIMARY KEY AUTOINCREMENT
  nome TEXT NOT NULL
  preco REAL NOT NULL
  estoque INTEGER NOT NULL
```

## Regras implementadas

- O ID e gerado automaticamente no cadastro.
- Uma atualizacao exige que o objeto tenha um ID.
- Busca por ID retorna um resultado opcional quando o registro nao existe.
- Atualizacao e remocao informam por retorno booleano se o registro foi encontrado.

Ainda nao existem validacoes para nome vazio, preco positivo ou estoque nao negativo.

## Proximas etapas

1. Adicionar validacoes de nome, preco e estoque.
2. Criar um menu interativo no terminal usando `Scanner`.
3. Implementar entrada e saida de estoque com verificacao de saldo.
4. Exibir mensagens adequadas quando um produto nao for encontrado.
5. Criar testes unitarios com JUnit 5.
6. Tornar o caminho do banco configuravel para facilitar os testes.
7. Avaliar a inclusao de categoria, marca e descricao no modelo.

## Evolucoes futuras

- cadastro de clientes e pets;
- agendamento de banho e tosa;
- vendas;
- interface grafica ou API REST.
