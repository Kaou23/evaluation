package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategorieService implements IDao<Categorie> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean create(Categorie categorie) {
        try {
            getSession().save(categorie);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Categorie findById(int id) {
        try {
            return getSession().get(Categorie.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Categorie> findAll() {
        try {
            return getSession().createQuery("FROM Categorie ORDER BY libelle").list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Categorie categorie) {
        try {
            getSession().update(categorie);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Categorie categorie) {
        try {
            getSession().delete(categorie);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rechercher une catégorie par code
     */
    public Categorie findByCode(String code) {
        try {
            return (Categorie) getSession()
                    .createQuery("FROM Categorie c WHERE c.code = :code")
                    .setParameter("code", code)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher toutes les catégories
     */
    public void afficherToutesLesCategories() {
        List<Categorie> categories = findAll();

        if (categories == null || categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée.");
            return;
        }

        System.out.println("\n=== Liste des catégories ===");
        System.out.printf("%-10s %-30s%n", "Code", "Libellé");
        System.out.println("----------------------------------------");

        for (Categorie c : categories) {
            System.out.printf("%-10s %-30s%n", c.getCode(), c.getLibelle());
        }
    }
}