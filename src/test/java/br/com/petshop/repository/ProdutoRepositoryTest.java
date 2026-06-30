package br.com.petshop.repository;

import br.com.petshop.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoRepositoryTest {

    @TempDir
    Path tempDir;

    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        Path databasePath = tempDir.resolve("petshop-test.db");
        produtoRepository = new ProdutoRepository("jdbc:sqlite:" + databasePath);
    }

    @Test
    void deveCadastrarEBuscarProdutoPorId() {
        Produto produto = new Produto("Racao Senior", 99.90, 7);

        Produto cadastrado = produtoRepository.cadastrar(produto);
        Optional<Produto> encontrado = produtoRepository.buscarPorId(cadastrado.getId());

        assertNotNull(cadastrado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Racao Senior", encontrado.get().getNome());
        assertEquals(7, encontrado.get().getEstoque());
    }

    @Test
    void deveListarProdutosEmOrdemDeCadastro() {
        produtoRepository.cadastrar(new Produto("Biscoito", 12.90, 20));
        produtoRepository.cadastrar(new Produto("Antipulgas", 79.90, 5));

        List<Produto> produtos = produtoRepository.listarTodos();

        assertEquals(2, produtos.size());
        assertEquals("Biscoito", produtos.get(0).getNome());
        assertEquals("Antipulgas", produtos.get(1).getNome());
    }

    @Test
    void deveAtualizarProdutoExistente() {
        Produto cadastrado = produtoRepository.cadastrar(new Produto("Comedouro", 18.00, 4));
        Produto produtoAtualizado = new Produto(cadastrado.getId(), "Comedouro Inox", 22.50, 6);

        boolean atualizado = produtoRepository.atualizar(produtoAtualizado);
        Optional<Produto> encontrado = produtoRepository.buscarPorId(cadastrado.getId());

        assertTrue(atualizado);
        assertTrue(encontrado.isPresent());
        assertEquals("Comedouro Inox", encontrado.get().getNome());
        assertEquals(22.50, encontrado.get().getPreco());
    }

    @Test
    void deveRemoverProdutoExistente() {
        Produto cadastrado = produtoRepository.cadastrar(new Produto("Cama Pet", 120.00, 2));

        boolean removido = produtoRepository.removerPorId(cadastrado.getId());

        assertTrue(removido);
        assertTrue(produtoRepository.buscarPorId(cadastrado.getId()).isEmpty());
    }

    @Test
    void deveRetornarFalseAoRemoverProdutoInexistente() {
        boolean removido = produtoRepository.removerPorId(999);

        assertFalse(removido);
    }
}
