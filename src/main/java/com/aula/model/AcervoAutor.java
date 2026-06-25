package com.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ACERVO_AUTOR")
public class AcervoAutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACERVO", nullable = false)
    private Acervo acervo;

    @Column(name = "AUTOR", nullable = false, length = 150)
    private String autor;

    public AcervoAutor() {}

    public AcervoAutor(Integer id, Acervo acervo, String autor) {
        this.id = id;
        this.acervo = acervo;
        this.autor = autor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Acervo getAcervo() {
        return acervo;
    }

    public void setAcervo(Acervo acervo) {
        this.acervo = acervo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
