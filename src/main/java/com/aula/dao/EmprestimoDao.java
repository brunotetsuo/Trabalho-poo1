package com.aula.dao;

import com.aula.model.Emprestimo;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class EmprestimoDao {

    public List<Emprestimo> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Emprestimo e JOIN FETCH e.usuario JOIN FETCH e.itemEmprestado", Emprestimo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> buscarAtivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Emprestimo e JOIN FETCH e.usuario JOIN FETCH e.itemEmprestado WHERE e.statusAtivo = 1", Emprestimo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> buscarAtivosPorMembro(int idMembro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                "SELECT e FROM Emprestimo e JOIN FETCH e.usuario JOIN FETCH e.itemEmprestado WHERE e.usuario.id = :idMembro AND e.statusAtivo = 1",
                Emprestimo.class
            );
            query.setParameter("idMembro", idMembro);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Emprestimo> buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Emprestimo emp = em.find(Emprestimo.class, id);
            return Optional.ofNullable(emp);
        } finally {
            em.close();
        }
    }

    public long contarAtivosPorMembro(int idMembro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario.id = :idMembro AND e.statusAtivo = 1",
                Long.class
            );
            query.setParameter("idMembro", idMembro);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public void salvar(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(emprestimo);
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
