# Java Petshop

## Objetivo

O Java Petshop e um projeto de estudo desenvolvido em Java para simular o gerenciamento de produtos de um petshop.
Atualmente, o sistema permite cadastrar, consultar, atualizar, remover produtos e controlar a movimentacao de estoque com persistencia em SQLite.

## Publico-alvo

Este projeto foi pensado como exercicio pratico de back-end e logica de negocio.

- estudantes de Java;
- desenvolvedores iniciantes;
- pessoas que querem praticar CRUD com JDBC e SQLite;
- quem deseja estudar organizacao em camadas `model`, `repository` e `service`.

## Funcionalidades principais

- cadastro de produtos;
- listagem de produtos;
- busca de produto por ID;
- atualizacao de produtos;
- remocao de produtos;
- adicao de estoque;
- remocao de estoque;
- validacoes de regras de negocio no servico;
- criacao automatica da tabela `produtos`.

## Stack

- Frontend: nao se aplica, aplicacao executada via terminal;
- Backend: Java 17;
- Banco de dados: SQLite;
- Infraestrutura: JDBC e Maven;
- Deploy: execucao local.

## Estrutura do projeto

```text
src/main/java/br/com/petshop/
  Main.java
  model/Produto.java
  repository/ProdutoRepository.java
  service/ProdutoService.java
```

## Como rodar localmente

### Pre-requisitos

- Java 17 instalado;
- Maven instalado.

### Passos

```bash
# instalar dependencias e compilar o projeto
mvn compile
```

Depois disso, execute a classe `br.com.petshop.Main` pela sua IDE.

Observacoes:

- o banco `petshop.db` e criado automaticamente na primeira execucao;
- nao ha necessidade de configurar variaveis de ambiente;
- nao ha migrations separadas neste projeto.

## Estado atual do projeto

Este projeto esta funcional para o escopo proposto e sera pausado por enquanto.
O foco principal foi consolidar a base da aplicacao com persistencia local, estrutura em camadas e operacoes centrais de estoque e produtos.

## Testes

O projeto possui 15 testes automatizados com JUnit 5, cobrindo regras de negocio do `ProdutoService` e operacoes principais do `ProdutoRepository`.

Para executar a suite:

```bash
mvn test
```

## Documentacao

- [Visao geral do projeto](docs/projeto.md)
- [Conceitos de Java](docs/java.md)

## Licenca

Este projeto esta sob a licenca definida no arquivo [LICENSE](LICENSE).
