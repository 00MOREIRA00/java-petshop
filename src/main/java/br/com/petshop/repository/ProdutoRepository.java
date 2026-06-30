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

    public ProdutoRepository() {
        this("jdbc:sqlite:petshop.db");
    }

    public ProdutoRepository(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        criarTabelaSeNaoExistir();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

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

    private Produto mapearProduto(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");
        double preco = resultSet.getDouble("preco");
        int estoque = resultSet.getInt("estoque");

        return new Produto(id, nome, preco, estoque);
    }
}
