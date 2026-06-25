package com.aula.model;

public interface Transacao {
    void registrar();
    boolean isAtiva();
}
