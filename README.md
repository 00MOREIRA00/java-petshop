# Java Petshop

Projeto de estudo em Java para gerenciar produtos de um petshop. A versao atual implementa persistencia em SQLite e operacoes basicas de cadastro, consulta, atualizacao e remocao.

## Tecnologias

- Java 17
- Maven
- SQLite
- JDBC
- JUnit 5 (configurado, ainda sem testes)

## Estado atual

O sistema possui:

- cadastro de produto;
- listagem de produtos;
- busca de produto por ID;
- atualizacao de produto;
- remocao de produto;
- criacao automatica da tabela `produtos` no SQLite.

O `Main` atual executa apenas uma demonstracao: cadastra um produto e lista os registros salvos. O menu interativo, as validacoes de negocio e as operacoes especificas de entrada e saida de estoque ainda nao foram implementados.

## Estrutura

```text
src/main/java/br/com/petshop/
  Main.java
  model/Produto.java
  repository/ProdutoRepository.java
  service/ProdutoService.java
```

O banco e criado no arquivo `petshop.db`, no diretorio em que a aplicacao for executada.

## Executar

Compile o projeto:

```powershell
mvn compile
```

Execute a classe `br.com.petshop.Main` pela IDE ou com um comando Maven configurado para execucao.

## Documentacao

- [Estado do projeto](docs/projeto.md)
- [Conceitos de Java](docs/java.md)
