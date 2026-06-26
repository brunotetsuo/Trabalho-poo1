package com.aula.service;

import com.aula.dao.AcervoDao;
import com.aula.dao.EmprestimoDao;
import com.aula.dao.ReservaDao;
import com.aula.model.Acervo;
import com.aula.model.Emprestimo;
import com.aula.model.Membro;
import com.aula.model.Reserva;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EmprestimoService {

    private final EmprestimoDao emprestimoDao = new EmprestimoDao();
    private final AcervoDao acervoDao = new AcervoDao();
    private final ReservaDao reservaDao = new ReservaDao();

    public List<Emprestimo> buscarTodos() {
        return emprestimoDao.buscarTodos();
    }

    public List<Emprestimo> buscarAtivos() {
        return emprestimoDao.buscarAtivos();
    }

    public List<Emprestimo> buscarAtivosPorMembro(int idMembro) {
        return emprestimoDao.buscarAtivosPorMembro(idMembro);
    }

    public List<Acervo> buscarItensDisponiveis() {
        return acervoDao.buscarTodos();
    }

    public Emprestimo realizarEmprestimo(Membro membro, Acervo acervo,
                                          LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        if (membro.isPunido()) {
            throw new IllegalStateException("Membro esta punido e nao pode realizar emprestimos.");
        }

        if (acervo.getStatusEmprestimo() != null && acervo.getStatusEmprestimo() == 1) {
            throw new IllegalStateException("Este item ja esta emprestado.");
        }

        if (acervo.getStatusReserva() != null && acervo.getStatusReserva() == 1) {
            Optional<Reserva> reserva = reservaDao.buscarAtivaPorAcervo(acervo.getId());
            if (reserva.isPresent() && reserva.get().getUsuarioInteressado().getId() != membro.getId()) {
                throw new IllegalStateException("Este item esta reservado por outro membro.");
            }
        }

        long emprestimosAtivos = emprestimoDao.contarAtivosPorMembro(membro.getId());
        if (emprestimosAtivos >= membro.getLimiteEmprestimos()) {
            throw new IllegalStateException("Limite de emprestimos atingido (" + membro.getLimiteEmprestimos() + ").");
        }

        long dias = ChronoUnit.DAYS.between(dataEmprestimo, dataDevolucao);
        if (dias < 1) {
            throw new IllegalStateException("A data de devolucao deve ser posterior a data de emprestimo.");
        }

        int limiteDias = "E".equals(membro.getTipoMembro()) ? 30 : 15;
        if (dias > limiteDias) {
            throw new IllegalStateException("Membro " + membro.getTipoMembro() + " pode pegar por no maximo " + limiteDias + " dias.");
        }

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(membro);
        emp.setItemEmprestado(acervo);
        emp.setDataExpiracao(Date.from(dataDevolucao.atStartOfDay().toInstant(ZoneOffset.UTC)));
        emp.registrar();

        acervo.setStatusEmprestimo(1);
        acervo.setStatusReserva(0);

        Optional<Reserva> reservaAtiva = reservaDao.buscarAtivaPorAcervo(acervo.getId());
        if (reservaAtiva.isPresent()) {
            Reserva r = reservaAtiva.get();
            r.setStatusAtivo(0);
            reservaDao.salvar(r);
        }

        emprestimoDao.salvar(emp);
        acervoDao.salvar(acervo);

        return emp;
    }

    public void devolverLivro(Emprestimo emprestimo) {
        if (emprestimo == null) {
            throw new IllegalStateException("Emprestimo nao encontrado.");
        }

        emprestimo.setStatusAtivo(0);
        emprestimoDao.salvar(emprestimo);

        Acervo acervo = emprestimo.getItemEmprestado();
        if (acervo != null) {
            acervo.setStatusEmprestimo(0);
            acervo.setStatusReserva(0);
            acervoDao.salvar(acervo);
        }
    }

    public void renovarEmprestimo(Emprestimo emprestimo, LocalDate novaDataDevolucao) {
        if (emprestimo == null) {
            throw new IllegalStateException("Emprestimo nao encontrado.");
        }

        if (!emprestimo.isAtiva()) {
            throw new IllegalStateException("Emprestimo nao esta ativo.");
        }

        Membro membro = emprestimo.getUsuario();
        if (membro != null && membro.isPunido()) {
            throw new IllegalStateException("Membro esta punido e nao pode renovar.");
        }

        int limiteRenovacoes = 3;
        if (emprestimo.getContagemRenovacoes() >= limiteRenovacoes) {
            throw new IllegalStateException("Limite de renovacoes atingido (" + limiteRenovacoes + ").");
        }

        emprestimo.setDataExpiracao(Date.from(novaDataDevolucao.atStartOfDay().toInstant(ZoneOffset.UTC)));
        emprestimo.setContagemRenovacoes(emprestimo.getContagemRenovacoes() + 1);

        emprestimoDao.salvar(emprestimo);
    }
}
