package se.kth.id1212.server.integration;

import se.kth.id1212.common.Catalog;
import se.kth.id1212.server.model.CatalogFile;
import se.kth.id1212.server.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2017-12-02.
 */
public class CatalogDAO {
    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    public CatalogDAO() {

        emFactory = Persistence.createEntityManagerFactory("catalogPersistenceUnit");
    }

    /**
     * Registers a new user.
     *
     * @param user The user to register.
     */
    public void registerUser(User user) {
        try {
            EntityManager em = beginTransaction();
            em.persist(user);
        } finally {
            commitTransaction();
        }
    }

    public User findUserByUsername(String username, boolean endTransactionAfterSearching) {
        if (username == null) {
            return null;
        }

        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findUserByUsername", User.class).
                        setParameter("username", username).getSingleResult();
            } catch (NoResultException noSuchAccount) {
                return null;
            }
        } finally {
            if (endTransactionAfterSearching) {
                commitTransaction();
            }
        }
    }

    public void uploadFile(CatalogFile file) {
        try {
            EntityManager em = beginTransaction();
            em.persist(file);
        } finally {
            commitTransaction();
        }
    }

    public CatalogFile findFileByName(String fileName, boolean endTransactionAfterSearching) {
        if (fileName == null) {
            return null;
        }

        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findFileByName", CatalogFile.class).
                        setParameter("fileName", fileName).getSingleResult();
            } catch (NoResultException noSuchAccount) {
                return null;
            }
        } finally {
            if (endTransactionAfterSearching) {
                commitTransaction();
            }
        }
    }

    public void updateFile() {
        commitTransaction();
    }

    public void deleteFile(String fileName) {
        try {
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteFileByName", CatalogFile.class).
                    setParameter("fileName", fileName).executeUpdate();
        } finally {
            commitTransaction();
        }
    }

    public void unregisterUser(String username) {
        try {
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteUserByUsername", User.class).
                    setParameter("username", username).executeUpdate();
        } finally {
            commitTransaction();
        }
    }

    /**
     * Retrieves all existing file.
     *
     * @return A list with all existing files. The list is empty if there are no files.
     */
    public List<CatalogFile> findAllFiles() {
        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findAllFiles", CatalogFile.class).getResultList();
            } catch (NoResultException noSuchAccount) {
                return new ArrayList<>();
            }
        } finally {
            commitTransaction();
        }
    }

    private EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        threadLocalEntityManager.set(em);
        EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return em;
    }

    private void commitTransaction() {
        threadLocalEntityManager.get().getTransaction().commit();
    }
}
