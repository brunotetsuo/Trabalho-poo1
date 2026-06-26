package com.aula.service;

import com.aula.dao.AcervoDao;
import com.aula.dao.ReservaDao;
import com.aula.model.Acervo;
import com.aula.model.Membro;
import com.aula.model.Reserva;

import java.util.List;

public class ReservaService {

    private final ReservaDao reservaDao = new ReservaDao();
    private final AcervoDao acervoDao = new AcervoDao();

    public List<Reserva> buscarTodos() {
        return reservaDao.buscarTodos();
    }

    public List<Reserva> buscarAtivosPorMembro(int idMembro) {
        return reservaDao.buscarAtivosPorMembro(idMembro);
    }

    public Reserva criarReserva(Membro membro, Acervo acervo) {
        if (acervo.getStatusReserva() != null && acervo.getStatusReserva() == 1) {
            throw new IllegalStateException("Este item ja possui reserva ativa.");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuarioInteressado(membro);
        reserva.setMaterial(acervo);
        reserva.registrar();

        acervo.setStatusReserva(1);

        reservaDao.salvar(reserva);
        acervoDao.salvar(acervo);

        return reserva;
    }

    public void cancelarReserva(Reserva reserva) {
        reserva.setStatusAtivo(0);
        reservaDao.salvar(reserva);

        Acervo acervo = reserva.getMaterial();
        if (acervo != null) {
            acervo.setStatusReserva(0);
            acervoDao.salvar(acervo);
        }
    }

    public boolean temReservaAtiva(Acervo acervo) {
        return acervo.getStatusReserva() != null && acervo.getStatusReserva() == 1;
    }

    public void limparReservasExpiradas() {
        List<Reserva> reservas = reservaDao.buscarTodos();
        for (Reserva reserva : reservas) {
            if (reserva.isAtiva() && reserva.getMaterial() != null) {
                Integer validade = reserva.getMaterial().getTempoValidadeReserva();
                if (validade != null && validade > 0) {
                    cancelarReserva(reserva);
                }
            }
        }
    }
}
