package com.aula.dao;

import com.aula.model.Reserva;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ReservaDao {

    public List<Reserva> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Reserva> buscarAtivosPorMembro(int idMembro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reserva> query = em.createQuery(
                "SELECT r FROM Reserva r WHERE r.usuarioInteressado.id = :idMembro AND r.statusAtivo = 1",
                Reserva.class
            );
            query.setParameter("idMembro", idMembro);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Reserva> buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Reserva res = em.find(Reserva.class, id);
            return Optional.ofNullable(res);
        } finally {
            em.close();
        }
    }

    public void salvar(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(reserva);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
