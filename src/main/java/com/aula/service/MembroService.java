package com.aula.service;

import com.aula.dao.MembroDao;
import com.aula.model.Membro;

import java.util.Optional;

public class MembroService {

    private final MembroDao membroDao = new MembroDao();

    public Optional<Membro> buscarPorLoginESenha(String login, String senha) {
        return membroDao.buscarPorLoginESenha(login, senha);
    }

    public Optional<Membro> buscarPorLogin(String login) {
        return membroDao.buscarPorLogin(login);
    }

    public Optional<Membro> buscarPorId(int id) {
        return membroDao.buscarPorId(id);
    }

    public void salvar(Membro membro) {
        membroDao.salvar(membro);
    }

    public Membro cadastrar(String nomeCompleto, String login, String senha, String tipoMembro) {
        Optional<Membro> existente = membroDao.buscarPorLogin(login);
        if (existente.isPresent()) {
            throw new IllegalStateException("Login ja cadastrado!");
        }

        Membro membro = new Membro();
        membro.setNomeCompleto(nomeCompleto);
        membro.setLogin(login);
        membro.setSenha(senha);
        membro.setPunido(false);
        membro.setTipoMembro(tipoMembro);

        boolean especial = "E".equals(tipoMembro);
        membro.setLimiteEmprestimos(especial ? 3 : 1);

        membroDao.salvar(membro);
        return membro;
    }
}
