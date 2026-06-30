package br.com.petshop.repository;

import br.com.petshop.model.Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepository {

    private final String databaseUrl;

    /**
     * Cria uma instância do repositório usando o banco de dados padrão da aplicação.
     */
    public ProdutoRepository() {
        this("jdbc:sqlite:petshop.db");
    }

    /**
     * Cria uma instância do repositório com a URL de banco de dados informada.
     *
     * @param databaseUrl URL de conexão com o banco de dados
     */
    public ProdutoRepository(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        criarTabelaSeNaoExistir();
    }

    /**
     * Abre uma conexão com o banco de dados configurado.
     *
     * @return conexão ativa com o banco de dados
     */
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    /**
     * Cria a tabela de produtos caso ela ainda não exista no banco de dados.
     */
    private void criarTabelaSeNaoExistir() {
        String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL,
                    estoque INTEGER NOT NULL
                );
                """;

        try (Connection connection = conectar();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela de produtos.", e);
        }
    }

    /**
     * Cadastra um novo produto no banco de dados.
     *
     * @param produto produto que será cadastrado
     * @return produto cadastrado com o identificador gerado
     */
    public Produto cadastrar(Produto produto) {
        String sql = """
                INSERT INTO produtos (nome, preco, estoque)
                VALUES (?, ?, ?);
                """;

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setDouble(2, produto.getPreco());
            preparedStatement.setInt(3, produto.getEstoque());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getInt(1));
                }
            }

            return produto;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar produto.", e);
        }
    }

    /**
     * Lista todos os produtos cadastrados no banco de dados.
     *
     * @return lista com todos os produtos encontrados
     */
    public List<Produto> listarTodos() {
        String sql = """
                SELECT id, nome, preco, estoque
                FROM produtos
                ORDER BY id;
                """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Produto produto = mapearProduto(resultSet);
                produtos.add(produto);
            }

            return produtos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos.", e);
        }
    }

    /**
     * Busca um produto pelo identificador.
     *
     * @param id identificador do produto
     * @return {@code Optional} contendo o produto quando ele for encontrado
     */
    public Optional<Produto> buscarPorId(Integer id) {
        String sql = """
                SELECT id, nome, preco, estoque
                FROM produtos
                WHERE id = ?;
                """;

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Produto produto = mapearProduto(resultSet);
                    return Optional.of(produto);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID.", e);
        }
    }

    /**
     * Atualiza os dados de um produto existente no banco de dados.
     *
     * @param produto produto com os dados atualizados
     * @return {@code true} quando ao menos um registro for atualizado
     */
    public boolean atualizar(Produto produto) {
        String sql = """
                UPDATE produtos
                SET nome = ?, preco = ?, estoque = ?
                WHERE id = ?;
                """;

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, produto.getNome());
            preparedStatement.setDouble(2, produto.getPreco());
            preparedStatement.setInt(3, produto.getEstoque());
            preparedStatement.setInt(4, produto.getId());

            int linhasAfetadas = preparedStatement.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto.", e);
        }
    }

    /**
     * Remove um produto do banco de dados com base no identificador.
     *
     * @param id identificador do produto
     * @return {@code true} quando ao menos um registro for removido
     */
    public boolean removerPorId(Integer id) {
        String sql = """
                DELETE FROM produtos
                WHERE id = ?;
                """;

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int linhasAfetadas = preparedStatement.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto.", e);
        }
    }

    /**
     * Converte o resultado da consulta em uma instância de produto.
     *
     * @param resultSet resultado da consulta posicionado no registro atual
     * @return produto mapeado a partir do registro atual
     */
    private Produto mapearProduto(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");
        double preco = resultSet.getDouble("preco");
        int estoque = resultSet.getInt("estoque");

        return new Produto(id, nome, preco, estoque);
    }
}
