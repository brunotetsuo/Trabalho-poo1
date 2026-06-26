package com.aula.service;

import com.aula.dao.AcervoDao;
import com.aula.model.Acervo;

import java.util.List;
import java.util.Optional;

public class AcervoService {

    private final AcervoDao acervoDao = new AcervoDao();

    public List<Acervo> buscarTodos() {
        return acervoDao.buscarTodos();
    }

    public Optional<Acervo> buscarPorId(int id) {
        return acervoDao.buscarPorId(id);
    }

    public List<Acervo> buscarPorTitulo(String titulo) {
        return acervoDao.buscarPorTitulo(titulo);
    }

    public List<String> buscarAutores(int idAcervo) {
        return acervoDao.buscarAutores(idAcervo);
    }

    public void salvar(Acervo acervo) {
        acervoDao.salvar(acervo);
    }

    public void atualizarStatusEmprestimo(Acervo acervo, int status) {
        acervo.setStatusEmprestimo(status);
        acervoDao.salvar(acervo);
    }

    public void atualizarStatusReserva(Acervo acervo, int status) {
        acervo.setStatusReserva(status);
        acervoDao.salvar(acervo);
    }

    public boolean isEmprestado(Acervo acervo) {
        return acervo.getStatusEmprestimo() != null && acervo.getStatusEmprestimo() == 1;
    }

    public boolean isReservado(Acervo acervo) {
        return acervo.getStatusReserva() != null && acervo.getStatusReserva() == 1;
    }
}
