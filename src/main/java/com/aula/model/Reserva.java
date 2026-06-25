package com.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RESERVA")
public class Reserva implements Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEMBRO", nullable = false)
    private Membro usuarioInteressado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACERVO", nullable = false)
    private Acervo material;

    @Column(name = "STATUS_ATIVO", nullable = false)
    private Integer statusAtivo;

    public Reserva() {
        this.statusAtivo = 1;
    }

    public Reserva(Integer id, Membro usuarioInteressado, Acervo material, Integer statusAtivo) {
        this.id = id;
        this.usuarioInteressado = usuarioInteressado;
        this.material = material;
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

    public Membro getUsuarioInteressado() {
        return usuarioInteressado;
    }

    public void setUsuarioInteressado(Membro usuarioInteressado) {
        this.usuarioInteressado = usuarioInteressado;
    }

    public Acervo getMaterial() {
        return material;
    }

    public void setMaterial(Acervo material) {
        this.material = material;
    }

    public Integer getStatusAtivo() {
        return statusAtivo;
    }

    public void setStatusAtivo(Integer statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    @Override
    public String toString() {
        String titulo = material != null ? material.getTitulo() : "N/A";
        return titulo + " | status: " + (isAtiva() ? "Ativa" : "Inativa");
    }
}
