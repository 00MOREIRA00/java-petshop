package br.com.petshop;

import br.com.petshop.model.Produto;
import br.com.petshop.repository.ProdutoRepository;
import br.com.petshop.service.ProdutoService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ProdutoRepository produtoRepository = new ProdutoRepository();
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        Scanner scanner = new Scanner(System.in);

        boolean executando = true;

        while (executando) {
            exibirMenu();

            int opcao = lerInteiro(scanner, "Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarProduto(scanner, produtoService);
                    case 2 -> listarProdutos(produtoService);
                    case 3 -> buscarProdutoPorId(scanner, produtoService);
                    case 4 -> atualizarProduto(scanner, produtoService);
                    case 5 -> removerProduto(scanner, produtoService);
                    case 6 -> adicionarEstoque(scanner, produtoService);
                    case 7 -> removerEstoque(scanner, produtoService);
                    case 0 -> {
                        System.out.println("Encerrando o sistema...");
                        executando = false;
                    }
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Dados inválidos: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Erro ao executar operação: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("===== Sistema Petshop =====");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Buscar produto por ID");
        System.out.println("4 - Atualizar produto");
        System.out.println("5 - Remover produto");
        System.out.println("6 - Adicionar estoque");
        System.out.println("7 - Remover estoque");
        System.out.println("0 - Sair");
    }

    private static void cadastrarProduto(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Cadastro de Produto =====");

        String nome = lerTexto(scanner, "Digite o nome do produto: ");
        double preco = lerDouble(scanner, "Digite o preço do produto: ");
        int estoque = lerInteiro(scanner, "Digite o estoque inicial: ");

        Produto produto = new Produto(nome, preco, estoque);

        Produto produtoCadastrado = produtoService.cadastrar(produto);

        System.out.println("Produto cadastrado com sucesso!");
        imprimirProduto(produtoCadastrado);
    }

    private static void listarProdutos(ProdutoService produtoService) {
        System.out.println("===== Lista de Produtos =====");

        List<Produto> produtos = produtoService.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (Produto produto : produtos) {
            imprimirProduto(produto);
        }
    }

    private static void buscarProdutoPorId(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Buscar Produto por ID =====");

        int id = lerInteiro(scanner, "Digite o ID do produto: ");

        Optional<Produto> produtoOptional = produtoService.buscarPorId(id);

        if (produtoOptional.isPresent()) {
            System.out.println("Produto encontrado:");
            imprimirProduto(produtoOptional.get());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void atualizarProduto(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Atualizar Produto =====");

        int id = lerInteiro(scanner, "Digite o ID do produto: ");

        Optional<Produto> produtoOptional = produtoService.buscarPorId(id);

        if (produtoOptional.isEmpty()) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Produto atual:");
        imprimirProduto(produtoOptional.get());

        String nome = lerTexto(scanner, "Digite o novo nome: ");
        double preco = lerDouble(scanner, "Digite o novo preço: ");
        int estoque = lerInteiro(scanner, "Digite o novo estoque: ");

        Produto produtoAtualizado = new Produto(id, nome, preco, estoque);

        boolean atualizado = produtoService.atualizar(produtoAtualizado);

        if (atualizado) {
            System.out.println("Produto atualizado com sucesso!");
            imprimirProduto(produtoAtualizado);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void removerProduto(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Remover Produto =====");

        int id = lerInteiro(scanner, "Digite o ID do produto: ");

        boolean removido = produtoService.removerPorId(id);

        if (removido) {
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void adicionarEstoque(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Adicionar Estoque =====");

        int id = lerInteiro(scanner, "Digite o ID do produto: ");
        int quantidade = lerInteiro(scanner, "Digite a quantidade para adicionar: ");

        boolean atualizado = produtoService.adicionarEstoque(id, quantidade);

        if (atualizado) {
            System.out.println("Estoque adicionado com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void removerEstoque(Scanner scanner, ProdutoService produtoService) {
        System.out.println("===== Remover Estoque =====");

        int id = lerInteiro(scanner, "Digite o ID do produto: ");
        int quantidade = lerInteiro(scanner, "Digite a quantidade para remover: ");

        boolean atualizado = produtoService.removerEstoque(id, quantidade);

        if (atualizado) {
            System.out.println("Estoque removido com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static String lerTexto(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);

            String entrada = scanner.nextLine();

            try {
                entrada = entrada.replace(",", ".");
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número decimal.");
            }
        }
    }

    private static void imprimirProduto(Produto produto) {
        System.out.println("-------------------------");
        System.out.println("ID: " + produto.getId());
        System.out.println("Nome: " + produto.getNome());
        System.out.printf("Preço: R$ %.2f%n", produto.getPreco());
        System.out.println("Estoque: " + produto.getEstoque());
    }
}