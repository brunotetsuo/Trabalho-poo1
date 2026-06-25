package com.aula.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMPRESTIMO")
public class Emprestimo implements Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEMBRO", nullable = false)
    private Membro usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACERVO", nullable = false)
    private Acervo itemEmprestado;

    @Column(name = "DATA_EXPIRACAO")
    private Date dataExpiracao;

    @Column(name = "CONTAGEM_RENOVACOES")
    private Integer contagemRenovacoes;

    @Column(name = "STATUS_ATIVO", nullable = false)
    private Integer statusAtivo;

    public Emprestimo() {
        this.contagemRenovacoes = 0;
        this.statusAtivo = 1;
    }

    public Emprestimo(Integer id, Membro usuario, Acervo itemEmprestado, Date dataExpiracao, Integer contagemRenovacoes, Integer statusAtivo) {
        this.id = id;
        this.usuario = usuario;
        this.itemEmprestado = itemEmprestado;
        this.dataExpiracao = dataExpiracao;
        this.contagemRenovacoes = contagemRenovacoes;
        this.statusAtivo = statusAtivo;
    }

    @Override
    public void registrar() {
        this.statusAtivo = 1;
    }

    @Override
    public boolean isAtiva() {
        return this.statusAtivo != null && this.statusAtivo == 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Membro getUsuario() {
        return usuario;
    }

    public void setUsuario(Membro usuario) {
        this.usuario = usuario;
    }

    public Acervo getItemEmprestado() {
        return itemEmprestado;
    }

    public void setItemEmprestado(Acervo itemEmprestado) {
        this.itemEmprestado = itemEmprestado;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Integer getContagemRenovacoes() {
        return contagemRenovacoes;
    }

    public void setContagemRenovacoes(Integer contagemRenovacoes) {
        this.contagemRenovacoes = contagemRenovacoes;
    }

    public Integer getStatusAtivo() {
        return statusAtivo;
    }

    public void setStatusAtivo(Integer statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    @Override
    public String toString() {
        String titulo = itemEmprestado != null ? itemEmprestado.getTitulo() : "N/A";
        return titulo + " | expira: " + dataExpiracao;
    }
}
