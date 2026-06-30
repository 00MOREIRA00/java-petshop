package br.com.petshop.service;

import br.com.petshop.model.Produto;
import br.com.petshop.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoServiceTest {

    private ProdutoService produtoService;
    private FakeProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        produtoRepository = new FakeProdutoRepository();
        produtoService = new ProdutoService(produtoRepository);
    }

    @Test
    void deveCadastrarProdutoValido() {
        Produto produto = new Produto("Racao Premium", 89.90, 10);

        Produto cadastrado = produtoService.cadastrar(produto);

        assertNotNull(cadastrado.getId());
        assertEquals("Racao Premium", cadastrado.getNome());
        assertEquals(1, produtoRepository.listarTodos().size());
    }

    @Test
    void deveLancarExcecaoAoCadastrarProdutoComNomeInvalido() {
        Produto produto = new Produto("   ", 89.90, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.cadastrar(produto)
        );

        assertEquals("Nome do produto é obrigatório.", exception.getMessage());
    }

    @Test
    void deveBuscarProdutoPorId() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Shampoo Pet", 25.00, 8));

        Optional<Produto> encontrado = produtoService.buscarPorId(cadastrado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Shampoo Pet", encontrado.get().getNome());
    }

    @Test
    void deveAtualizarProdutoExistente() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Areia Higienica", 30.00, 12));
        Produto atualizado = new Produto(cadastrado.getId(), "Areia Higienica Plus", 35.00, 15);

        boolean resultado = produtoService.atualizar(atualizado);

        assertTrue(resultado);
        Optional<Produto> produtoAtualizado = produtoService.buscarPorId(cadastrado.getId());
        assertTrue(produtoAtualizado.isPresent());
        assertEquals("Areia Higienica Plus", produtoAtualizado.get().getNome());
        assertEquals(15, produtoAtualizado.get().getEstoque());
    }

    @Test
    void deveAdicionarEstoqueAoProdutoExistente() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Coleira", 19.90, 3));

        boolean resultado = produtoService.adicionarEstoque(cadastrado.getId(), 5);

        assertTrue(resultado);
        assertEquals(8, produtoService.buscarPorId(cadastrado.getId()).orElseThrow().getEstoque());
    }

    @Test
    void deveRetornarFalseAoAdicionarEstoqueDeProdutoInexistente() {
        boolean resultado = produtoService.adicionarEstoque(999, 2);

        assertFalse(resultado);
    }

    @Test
    void deveRemoverEstoqueDoProdutoExistente() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Brinquedo", 14.50, 9));

        boolean resultado = produtoService.removerEstoque(cadastrado.getId(), 4);

        assertTrue(resultado);
        assertEquals(5, produtoService.buscarPorId(cadastrado.getId()).orElseThrow().getEstoque());
    }

    @Test
    void deveLancarExcecaoAoRemoverEstoqueInsuficiente() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Tapete Higienico", 49.90, 2));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.removerEstoque(cadastrado.getId(), 3)
        );

        assertEquals("Estoque insuficiente para remover essa quantidade.", exception.getMessage());
    }

    @Test
    void deveRemoverProdutoPorId() {
        Produto cadastrado = produtoService.cadastrar(new Produto("Peitoral", 39.90, 6));

        boolean removido = produtoService.removerPorId(cadastrado.getId());

        assertTrue(removido);
        assertTrue(produtoService.buscarPorId(cadastrado.getId()).isEmpty());
    }

    @Test
    void deveLancarExcecaoAoBuscarComIdInvalido() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.buscarPorId(0)
        );

        assertEquals("ID do produto deve ser maior que zero.", exception.getMessage());
    }

    private static class FakeProdutoRepository extends ProdutoRepository {

        private final Map<Integer, Produto> produtos = new LinkedHashMap<>();
        private int proximoId = 1;

        FakeProdutoRepository() {
            super("jdbc:sqlite::memory:");
        }

        @Override
        public Produto cadastrar(Produto produto) {
            produto.setId(proximoId++);
            produtos.put(produto.getId(), copiar(produto));
            return produto;
        }

        @Override
        public List<Produto> listarTodos() {
            List<Produto> resultado = new ArrayList<>();

            for (Produto produto : produtos.values()) {
                resultado.add(copiar(produto));
            }

            return resultado;
        }

        @Override
        public Optional<Produto> buscarPorId(Integer id) {
            Produto produto = produtos.get(id);
            return produto == null ? Optional.empty() : Optional.of(copiar(produto));
        }

        @Override
        public boolean atualizar(Produto produto) {
            if (!produtos.containsKey(produto.getId())) {
                return false;
            }

            produtos.put(produto.getId(), copiar(produto));
            return true;
        }

        @Override
        public boolean removerPorId(Integer id) {
            return produtos.remove(id) != null;
        }

        private Produto copiar(Produto produto) {
            return new Produto(
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getEstoque()
            );
        }
    }
}
