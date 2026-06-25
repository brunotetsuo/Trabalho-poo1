package com.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ACERVO")
public class Acervo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITULO", nullable = false, length = 200)
    private String titulo;

    @Column(name = "ANO_PUBLICACAO")
    private Integer anoPublicacao;

    @Column(name = "VALOR_PENALIDADE")
    private Double valorPenalidade;

    @Column(name = "TEMPO_VALIDADE_RESERVA")
    private Integer tempoValidadeReserva;

    @Column(name = "STATUS_EMPRESTIMO", nullable = false)
    private Integer statusEmprestimo;

    @Column(name = "STATUS_RESERVA", nullable = false)
    private Integer statusReserva;

    @Column(name = "TIPO_ACERVO", nullable = false, length = 20)
    private String tipoAcervo;

    public Acervo() {}

    public Acervo(Integer id, String titulo, Integer anoPublicacao, Double valorPenalidade,
                  Integer tempoValidadeReserva, Integer statusEmprestimo, Integer statusReserva, String tipoAcervo) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.valorPenalidade = valorPenalidade;
        this.tempoValidadeReserva = tempoValidadeReserva;
        this.statusEmprestimo = statusEmprestimo;
        this.statusReserva = statusReserva;
        this.tipoAcervo = tipoAcervo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Double getValorPenalidade() {
        return valorPenalidade;
    }

    public void setValorPenalidade(Double valorPenalidade) {
        this.valorPenalidade = valorPenalidade;
    }

    public Integer getTempoValidadeReserva() {
        return tempoValidadeReserva;
    }

    public void setTempoValidadeReserva(Integer tempoValidadeReserva) {
        this.tempoValidadeReserva = tempoValidadeReserva;
    }

    public Integer getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public void setStatusEmprestimo(Integer statusEmprestimo) {
        this.statusEmprestimo = statusEmprestimo;
    }

    public Integer getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(Integer statusReserva) {
        this.statusReserva = statusReserva;
    }

    public String getTipoAcervo() {
        return tipoAcervo;
    }

    public void setTipoAcervo(String tipoAcervo) {
        this.tipoAcervo = tipoAcervo;
    }

    @Override
    public String toString() {
        return titulo + " (" + tipoAcervo + ")";
    }
}
