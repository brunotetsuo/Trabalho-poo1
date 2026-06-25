package com.aula.dao;

import com.aula.model.Acervo;
import com.aula.model.AcervoAutor;
import com.aula.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AcervoDao {

    public List<Acervo> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Acervo a", Acervo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Acervo> buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Acervo acervo = em.find(Acervo.class, id);
            return Optional.ofNullable(acervo);
        } finally {
            em.close();
        }
    }

    public List<Acervo> buscarPorTitulo(String titulo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Acervo> query = em.createQuery(
                "SELECT a FROM Acervo a WHERE LOWER(a.titulo) LIKE LOWER(:titulo)",
                Acervo.class
            );
            query.setParameter("titulo", "%" + titulo + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> buscarAutores(int idAcervo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT aa.autor FROM AcervoAutor aa WHERE aa.acervo.id = :idAcervo",
                String.class
            );
            query.setParameter("idAcervo", idAcervo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void salvar(Acervo acervo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(acervo);
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
