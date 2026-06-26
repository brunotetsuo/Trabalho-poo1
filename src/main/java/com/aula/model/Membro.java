package com.aula.model;

import com.aula.util.BooleanIntegerConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "MEMBRO")
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_MEMBRO_ID")
    @SequenceGenerator(name = "GEN_MEMBRO_ID", sequenceName = "GEN_MEMBRO_ID", allocationSize = 1)
    @Column(name = "ID")
    private int id;

    @Column(name = "NOME_COMPLETO", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "LOGIN", nullable = false, length = 50)
    private String login;

    @Column(name = "SENHA", nullable = false, length = 100)
    private String senha;

    @Column(name = "IS_PUNIDO", nullable = false)
    @Convert(converter = BooleanIntegerConverter.class)
    private boolean isPunido;

    @Column(name = "LIMITE_EMPRESTIMOS", nullable = false)
    private Integer limiteEmprestimos;

    @Column(name = "TIPO_MEMBRO", nullable = false, length = 1)
    private String tipoMembro;

    public Membro() {}

    public Membro(int id, String nomeCompleto, String login, String senha, boolean isPunido, Integer limiteEmprestimos, String tipoMembro) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.login = login;
        this.senha = senha;
        this.isPunido = isPunido;
        this.limiteEmprestimos = limiteEmprestimos;
        this.tipoMembro = tipoMembro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isPunido() {
        return isPunido;
    }

    public void setPunido(boolean punido) {
        isPunido = punido;
    }

    public Integer getLimiteEmprestimos() {
        return limiteEmprestimos;
    }

    public void setLimiteEmprestimos(Integer limiteEmprestimos) {
        this.limiteEmprestimos = limiteEmprestimos;
    }

    public String getTipoMembro() {
        return tipoMembro;
    }

    public void setTipoMembro(String tipoMembro) {
        this.tipoMembro = tipoMembro;
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }
}
