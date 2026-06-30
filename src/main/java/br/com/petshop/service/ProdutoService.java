package br.com.petshop.service;

import br.com.petshop.model.Produto;
import br.com.petshop.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    /**
     * Cria uma nova instância do serviço de produtos.
     *
     * @param produtoRepository repositório responsável pela persistência dos produtos
     */
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Cadastra um novo produto após validar seus dados.
     *
     * @param produto produto que será cadastrado
     * @return produto cadastrado no repositório
     */
    public Produto cadastrar(Produto produto) {
        validarProduto(produto);

        return produtoRepository.cadastrar(produto);
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return lista com todos os produtos
     */
    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }

    /**
     * Busca um produto pelo identificador.
     *
     * @param id identificador do produto
     * @return {@code Optional} contendo o produto, caso encontrado
     */
    public Optional<Produto> buscarPorId(Integer id) {
        validarId(id);

        return produtoRepository.buscarPorId(id);
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param produto produto com os dados atualizados
     * @return {@code true} quando a atualização for realizada com sucesso
     */
    public boolean atualizar(Produto produto) {
        validarId(produto.getId());
        validarProduto(produto);

        return produtoRepository.atualizar(produto);
    }

    /**
     * Remove um produto com base no identificador informado.
     *
     * @param id identificador do produto
     * @return {@code true} quando o produto for removido com sucesso
     */
    public boolean removerPorId(Integer id) {
        validarId(id);

        return produtoRepository.removerPorId(id);
    }

    /**
     * Adiciona uma quantidade ao estoque de um produto.
     *
     * @param id identificador do produto
     * @param quantidade quantidade que será adicionada ao estoque
     * @return {@code true} quando o estoque for atualizado com sucesso
     */
    public boolean adicionarEstoque(Integer id, int quantidade) {
        validarId(id);
        validarQuantidadeMovimentacao(quantidade);

        Optional<Produto> produtoOptional = produtoRepository.buscarPorId(id);

        if (produtoOptional.isEmpty()) {
            return false;
        }

        Produto produto = produtoOptional.get();
        produto.setEstoque(produto.getEstoque() + quantidade);

        return produtoRepository.atualizar(produto);
    }

    /**
     * Remove uma quantidade do estoque de um produto.
     *
     * @param id identificador do produto
     * @param quantidade quantidade que será removida do estoque
     * @return {@code true} quando o estoque for atualizado com sucesso
     */
    public boolean removerEstoque(Integer id, int quantidade){
        validarId(id);
        validarQuantidadeMovimentacao(quantidade);

        Optional<Produto> produtoOptional = produtoRepository.buscarPorId(id);

        if (produtoOptional.isEmpty()){
            return false;
        }

        Produto produto = produtoOptional.get();

        if (produto.getEstoque() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para remover essa quantidade.");
        }

        produto.setEstoque(produto.getEstoque() - quantidade);

        return produtoRepository.atualizar(produto);

    }

    /**
     * Valida os dados obrigatórios de um produto.
     *
     * @param produto produto que será validado
     */
    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }

        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero.");
        }

        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto não pode ser negativo.");
        }
    }

    /**
     * Valida o identificador de um produto.
     *
     * @param id identificador que será validado
     */
    private void validarId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("ID do produto deve ser maior que zero.");
        }
    }

    /**
     * Valida a quantidade informada para movimentação de estoque.
     *
     * @param quantidade quantidade que será validada
     */
    private void validarQuantidadeMovimentacao(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
    }

}
