package com.aula.util;

import com.aula.model.Membro;

public class Sessao {

    private static Membro membroLogado;

    public static Membro getMembroLogado() {
        return membroLogado;
    }

    public static void setMembroLogado(Membro membro) {
        membroLogado = membro;
    }

    public static void limpar() {
        membroLogado = null;
    }
}
