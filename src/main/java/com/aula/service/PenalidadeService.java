package com.aula.service;

import com.aula.dao.AcervoDao;
import com.aula.dao.EmprestimoDao;
import com.aula.dao.MembroDao;
import com.aula.model.Acervo;
import com.aula.model.Emprestimo;
import com.aula.model.Membro;

import java.util.Date;
import java.util.List;

public class PenalidadeService {

    private final EmprestimoDao emprestimoDao = new EmprestimoDao();
    private final MembroDao membroDao = new MembroDao();
    private final AcervoDao acervoDao = new AcervoDao();

    public void verificarEPenalizarAtrasos() {
        List<Emprestimo> emprestimos = emprestimoDao.buscarTodos();
        Date agora = new Date();

        for (Emprestimo emp : emprestimos) {
            if (emp.isAtiva() && emp.getDataExpiracao() != null
                    && emp.getDataExpiracao().before(agora)) {
                Membro membro = emp.getUsuario();
                if (membro != null && !membro.isPunido()) {
                    membro.setPunido(true);
                    membroDao.salvar(membro);
                }
            }
        }
    }

    public void aplicarPenalidade(Membro membro) {
        membro.setPunido(true);
        membroDao.salvar(membro);
    }

    public void removerPenalidade(Membro membro) {
        membro.setPunido(false);
        membroDao.salvar(membro);
    }
}
