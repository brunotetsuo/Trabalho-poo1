package com.aula.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class BibliotecaDados {
    private static final List<Livro> LIVROS = new ArrayList<>();
    private static final List<Emprestimo> EMPRESTIMOS = new ArrayList<>();
    private static Livro livroSelecionado;
    private static Usuario usuarioLogado;

    static {
        LIVROS.add(new Livro("Java Basico", "Ana Martins", "Programacao"));
        LIVROS.add(new Livro("Banco de Dados", "Carlos Silva", "Tecnologia"));
        LIVROS.add(new Livro("Estrutura de Dados", "Mariana Costa", "Programacao"));
        LIVROS.add(new Livro("Algoritmos", "Paulo Souza", "Programacao"));
        LIVROS.add(new Livro("Clean Code", "Robert C. Martin", "Programacao"));
        LIVROS.add(new Livro("Padroes de Projeto", "Erich Gamma", "Engenharia de Software"));
        LIVROS.add(new Livro("Engenharia de Software", "Ian Sommerville", "Tecnologia"));
        LIVROS.add(new Livro("Redes de Computadores", "Andrew Tanenbaum", "Tecnologia"));
        LIVROS.add(new Livro("Sistemas Operacionais", "Abraham Silberschatz", "Tecnologia"));
        LIVROS.add(new Livro("HTML e CSS", "Jon Duckett", "Web"));
        LIVROS.add(new Livro("JavaScript Moderno", "David Flanagan", "Web"));
        LIVROS.add(new Livro("Python para Iniciantes", "Luciano Ramalho", "Programacao"));
        LIVROS.add(new Livro("Logica de Programacao", "Andre Luiz", "Programacao"));
        LIVROS.add(new Livro("Inteligencia Artificial", "Stuart Russell", "Tecnologia"));
        LIVROS.add(new Livro("Aprendizado de Maquina", "Aurelien Geron", "Tecnologia"));
        LIVROS.add(new Livro("Seguranca da Informacao", "William Stallings", "Tecnologia"));
        LIVROS.add(new Livro("Arquitetura Limpa", "Robert C. Martin", "Engenharia de Software"));
        LIVROS.add(new Livro("Domain-Driven Design", "Eric Evans", "Engenharia de Software"));
        LIVROS.add(new Livro("Refatoracao", "Martin Fowler", "Engenharia de Software"));
        LIVROS.add(new Livro("Use a Cabeca Java", "Kathy Sierra", "Programacao"));
        LIVROS.add(new Livro("Matematica Discreta", "Kenneth Rosen", "Matematica"));
        LIVROS.add(new Livro("Calculo I", "James Stewart", "Matematica"));
        LIVROS.add(new Livro("Fisica Basica", "Halliday Resnick", "Ciencias"));
        LIVROS.add(new Livro("Administracao Moderna", "Idalberto Chiavenato", "Administracao"));
        LIVROS.add(new Livro("O Senhor dos Aneis", "J. R. R. Tolkien", "Literatura"));
    }

    private BibliotecaDados() {
    }

    public static List<Livro> listarLivros() {
        return Collections.unmodifiableList(LIVROS);
    }

    public static List<Livro> buscarLivros(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarLivros();
        }

        String busca = termo.toLowerCase(Locale.ROOT).trim();
        return LIVROS.stream()
                .filter(livro -> livro.getTitulo().toLowerCase(Locale.ROOT).contains(busca)
                        || livro.getAutor().toLowerCase(Locale.ROOT).contains(busca)
                        || livro.getCategoria().toLowerCase(Locale.ROOT).contains(busca))
                .collect(Collectors.toList());
    }

    public static Livro getLivroSelecionado() {
        return livroSelecionado;
    }

    public static void setLivroSelecionado(Livro livroSelecionado) {
        BibliotecaDados.livroSelecionado = livroSelecionado;
    }

    public static void registrarEmprestimo(Emprestimo emprestimo) {
        emprestimo.getLivro().setDisponivel(false);
        EMPRESTIMOS.add(emprestimo);
    }

    public static List<Emprestimo> listarEmprestimos() {
        return Collections.unmodifiableList(EMPRESTIMOS);
    }

    public static List<Emprestimo> listarEmprestimosDoUsuarioLogado() {
        if (usuarioLogado == null) {
            return listarEmprestimos();
        }

        return EMPRESTIMOS.stream()
                .filter(emprestimo -> emprestimo.getUsuario().equalsIgnoreCase(usuarioLogado.getNome()))
                .collect(Collectors.toList());
    }

    public static void devolver(Emprestimo emprestimo) {
        emprestimo.getLivro().setDisponivel(true);
        EMPRESTIMOS.remove(emprestimo);
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        BibliotecaDados.usuarioLogado = usuarioLogado;
    }

    public static String getTipoMembroUsuarioLogado() {
        return usuarioLogado == null ? "COMUM" : usuarioLogado.getTipoMembro();
    }

    public static String getNomeUsuarioLogado() {
        return usuarioLogado == null ? "" : usuarioLogado.getNome();
    }
}
