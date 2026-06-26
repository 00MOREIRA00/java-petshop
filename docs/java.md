# Conceitos de Java no projeto

Este projeto pratica fundamentos de Java por meio de uma aplicacao pequena com persistencia em SQLite.

## Conceitos usados atualmente

### Classes e objetos

- `Produto` representa um item do petshop.
- `ProdutoService` organiza as operacoes disponiveis para produtos.
- `ProdutoRepository` concentra o acesso ao banco de dados.
- `Main` cria os objetos e executa uma demonstracao.

O modelo utiliza atributos privados, construtores, getters, setters, sobrecarga de construtores e sobrescrita de `toString`.

### Pacotes

O codigo esta separado nos pacotes:

- `br.com.petshop`;
- `br.com.petshop.model`;
- `br.com.petshop.service`;
- `br.com.petshop.repository`.

### Colecoes e tipos opcionais

O repositorio usa:

- `List<Produto>` e `ArrayList<Produto>` para devolver varios registros;
- `Optional<Produto>` para representar uma busca que pode nao encontrar um produto.

Os produtos nao sao armazenados permanentemente na lista. A persistencia e feita no SQLite, e a lista apenas reune os resultados de uma consulta.

### JDBC e banco de dados

O projeto usa JDBC para acessar o SQLite. Os principais tipos praticados sao:

- `Connection` para abrir uma conexao;
- `PreparedStatement` para executar comandos SQL com parametros;
- `Statement` para criar a tabela;
- `ResultSet` para ler resultados;
- `SQLException` para representar falhas do banco;
- `try-with-resources` para fechar recursos automaticamente.

As operacoes SQL implementadas incluem `CREATE TABLE`, `INSERT`, `SELECT`, `UPDATE` e `DELETE`.

### Excecoes

Erros de acesso ao SQLite sao convertidos em `RuntimeException` com uma mensagem contextual. A atualizacao de um produto sem ID lanca `IllegalArgumentException`.

Uma evolucao possivel e criar excecoes especificas do dominio.

### Injecao de dependencia por construtor

`ProdutoService` pode criar seu proprio repositorio ou receber um `ProdutoRepository` no construtor. Essa separacao reduz o acoplamento, embora o repositorio ainda use um caminho fixo para o banco.

## Conceitos planejados

As proximas etapas devem introduzir:

- `Scanner` para entrada de dados;
- `switch` e lacos para o menu interativo;
- validacoes com `if` e lancamento de excecoes;
- metodos especificos para entrada e saida de estoque;
- testes unitarios com JUnit 5.

JUnit 5 ja esta configurado no `pom.xml`, mas o projeto ainda nao possui classes de teste.

## Sequencia sugerida de estudo

1. Entender o fluxo entre `Main`, servico e repositorio.
2. Acompanhar o CRUD e o mapeamento entre linhas do banco e objetos.
3. Implementar e testar as regras de negocio.
4. Criar o menu do terminal.
5. Escrever testes com um banco separado e configuravel.
6. Evoluir o modelo e as funcionalidades do petshop.
