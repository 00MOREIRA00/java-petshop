package br.com.petshop;

import br.com.petshop.model.Produto;
import br.com.petshop.repository.ProdutoRepository;
import br.com.petshop.service.ProdutoService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bem-vindo ao PetShop!");
        System.out.println("-----------------------");

        ProdutoRepository produtoRepository = new ProdutoRepository();

        Produto produto = new Produto("Pato de Burracha", 15.90, 50);

        produtoRepository.cadastrar(produto);

        System.out.println("Produto cadastrado:");
        System.out.println(produto);

        System.out.println("Produtos no banco:");
        produtoRepository.listarTodos().forEach(System.out::println);
    }
}
