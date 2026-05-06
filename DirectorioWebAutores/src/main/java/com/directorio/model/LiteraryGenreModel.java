package com.directorio.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LiteraryGenreModel {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("AuthorsPU");

    public List<LiteraryGenre> findAllGenres() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT g FROM LiteraryGenre g", LiteraryGenre.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public LiteraryGenre findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(LiteraryGenre.class, id);
        } finally {
            em.close();
        }
    }
}