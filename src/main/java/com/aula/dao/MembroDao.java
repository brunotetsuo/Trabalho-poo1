package com.aula.dao;

import com.aula.model.Membro;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class MembroDao {

    public Optional<Membro> buscarPorLoginESenha(String login, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Membro> query = em.createQuery(
                "SELECT m FROM Membro m WHERE m.login = :login AND m.senha = :senha",
                Membro.class
            );
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public Optional<Membro> buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Membro membro = em.find(Membro.class, id);
            return Optional.ofNullable(membro);
        } finally {
            em.close();
        }
    }

    public Optional<Membro> buscarPorLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Membro> query = em.createQuery(
                "SELECT m FROM Membro m WHERE m.login = :login",
                Membro.class
            );
            query.setParameter("login", login);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Membro> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Membro m", Membro.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void salvar(Membro membro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(membro);
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
