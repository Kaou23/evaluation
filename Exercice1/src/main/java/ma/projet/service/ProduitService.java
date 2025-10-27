package ma.projet.service;

import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProduitService implements IDao<Produit> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean create(Produit produit) {
        try {
            getSession().save(produit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Produit findById(int id) {
        try {
            return getSession().get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Produit> findAll() {
        try {
            return getSession().createQuery("FROM Produit").list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Produit produit) {
        try {
            getSession().update(produit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Produit produit) {
        try {
            getSession().delete(produit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Afficher la liste des produits par catégorie
     * @param categorieId ID de la catégorie
     * @return Liste des produits de cette catégorie
     */
    @SuppressWarnings("unchecked")
    public List<Produit> findByCategorie(int categorieId) {
        try {
            return getSession()
                    .createQuery("FROM Produit p WHERE p.categorie.id = :catId ORDER BY p.reference")
                    .setParameter("catId", categorieId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits commandés entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des produits commandés dans cette période
     */
    @SuppressWarnings("unchecked")
    public List<Produit> findProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        try {
            String hql = "SELECT DISTINCT p FROM Produit p " +
                    "JOIN p.lignesCommande lcp " +
                    "JOIN lcp.commande c " +
                    "WHERE c.date BETWEEN :debut AND :fin " +
                    "ORDER BY p.reference";

            return getSession()
                    .createQuery(hql)
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits dont le prix est supérieur à 100 DH
     * Utilise une requête nommée
     * @return Liste des produits avec prix > 100 DH
     */
    @SuppressWarnings("unchecked")
    public List<Produit> findProduitsSuperieur100() {
        try {
            return getSession()
                    .getNamedQuery("Produit.findByPrixSuperieur")
                    .setParameter("prix", 100.0f)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits par catégorie avec détails
     */
    public void afficherProduitsParCategorie(int categorieId) {
        List<Produit> produits = findByCategorie(categorieId);

        if (produits == null || produits.isEmpty()) {
            System.out.println("Aucun produit trouvé pour cette catégorie.");
            return;
        }

        System.out.println("\n=== Produits de la catégorie : " + produits.get(0).getCategorie().getLibelle() + " ===");
        System.out.printf("%-20s %-15s%n", "Référence", "Prix");
        System.out.println("----------------------------------------");

        for (Produit p : produits) {
            System.out.printf("%-20s %-15.2f DH%n", p.getReference(), p.getPrix());
        }
    }
}