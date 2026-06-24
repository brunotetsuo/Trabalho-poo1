package com.aula.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private final Livro livro;
    private final String usuario;
    private final String tipoMembro;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataDevolucao;

    public Emprestimo(Livro livro, String usuario, String tipoMembro, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.livro = livro;
        this.usuario = usuario;
        this.tipoMembro = tipoMembro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public Livro getLivro() {
        return livro;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipoMembro() {
        return tipoMembro;
    }

    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataDevolucao);
    }

    @Override
    public String toString() {
        return livro.getTitulo()
                + " - Usuario: " + usuario
                + " - Membro: " + tipoMembro
                + " - Emprestimo: " + dataEmprestimo
                + " - Devolucao: " + dataDevolucao
                + " - Dias restantes: " + getDiasRestantes();
    }
}
