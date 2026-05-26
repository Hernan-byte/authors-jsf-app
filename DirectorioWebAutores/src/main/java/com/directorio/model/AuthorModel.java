package com.directorio.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AuthorModel {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("AuthorsPU");

    public void create(Author author) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(Author author) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(author);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Author author = em.find(Author.class, id);
            if (author != null) {
                em.remove(author);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Author> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } finally {
            em.close();
        }
    }

<<<<<<< HEAD
    // Filtro por género (punto 6a - AJAX con selectOneMenu)
    public List<Author> findByGenre(Integer genreId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM Author a WHERE a.genre.id = :genreId", Author.class)
                    .setParameter("genreId", genreId)
                    .getResultList();
=======
    // Método útil para el filtro asíncrono con AJAX (Punto 6a del Controlador)
    public List<Author> findByGenre(Integer genreId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Author a WHERE a.genre.id = :genreId", Author.class)
                     .setParameter("genreId", genreId)
                     .getResultList();
>>>>>>> cb6031fa78be5efa5577eff92ef1992106d11635
        } finally {
            em.close();
        }
    }
<<<<<<< HEAD

    // Filtro por nombre (punto 6c - AJAX con inputText)
    public List<Author> findByName(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM Author a WHERE LOWER(a.name) LIKE :nombre", Author.class)
                    .setParameter("nombre", "%" + nombre.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
=======
}
>>>>>>> cb6031fa78be5efa5577eff92ef1992106d11635
