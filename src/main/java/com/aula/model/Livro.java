package com.aula.model;

public class Livro {
    private final String titulo;
    private final String autor;
    private final String categoria;
    private boolean disponivel;

    public Livro(String titulo, String autor, String categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.disponivel = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getStatus() {
        return disponivel ? "Disponivel" : "Indisponivel";
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + categoria + ") - " + getStatus();
    }
}
