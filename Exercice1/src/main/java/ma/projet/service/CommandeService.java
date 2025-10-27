package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommandeService implements IDao<Commande> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean create(Commande commande) {
        try {
            getSession().save(commande);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Commande findById(int id) {
        try {
            return getSession().get(Commande.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Commande> findAll() {
        try {
            return getSession().createQuery("FROM Commande ORDER BY date DESC").list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Commande commande) {
        try {
            getSession().update(commande);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Commande commande) {
        try {
            getSession().delete(commande);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Trouver les commandes entre deux dates
     */
    @SuppressWarnings("unchecked")
    public List<Commande> findByDateRange(Date dateDebut, Date dateFin) {
        try {
            return getSession()
                    .createQuery("FROM Commande c WHERE c.date BETWEEN :debut AND :fin ORDER BY c.date DESC")
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les produits commandés dans une commande donnée
     * Format d'affichage :
     * Commande : 4     Date : 14 Mars 2013
     * Liste des produits :
     * Référence   Prix    Quantité
     * ES12        120 DH  7
     */
    public void afficherProduitsCommande(int commandeId) {
        try {
            Commande commande = findById(commandeId);

            if (commande == null) {
                System.out.println("Commande introuvable.");
                return;
            }

            // Format de la date en français "14 Mars 2013"
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("fr"));

            // Affichage de l'en-tête
            System.out.println("\nCommande : " + commande.getId() +
                    "     Date : " + sdf.format(commande.getDate()));
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-10s %-10s%n", "Référence", "Prix", "Quantité");

            // Charger les lignes de commande avec une requête optimisée
            @SuppressWarnings("unchecked")
            List<LigneCommandeProduit> lignes = getSession()
                    .createQuery("FROM LigneCommandeProduit lcp " +
                            "JOIN FETCH lcp.produit " +
                            "WHERE lcp.commande.id = :cmdId " +
                            "ORDER BY lcp.produit.reference")
                    .setParameter("cmdId", commandeId)
                    .list();

            if (lignes == null || lignes.isEmpty()) {
                System.out.println("Aucun produit dans cette commande.");
                return;
            }

            // Affichage des produits
            for (LigneCommandeProduit ligne : lignes) {
                System.out.printf("%-12s %-10.0f DH %-10d%n",
                        ligne.getProduit().getReference(),
                        ligne.getProduit().getPrix(),
                        ligne.getQuantite());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Afficher toutes les commandes
     */
    public void afficherToutesLesCommandes() {
        List<Commande> commandes = findAll();

        if (commandes == null || commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("\n=== Liste des commandes ===");
        System.out.printf("%-10s %-20s%n", "ID", "Date");
        System.out.println("----------------------------------------");

        for (Commande c : commandes) {
            System.out.printf("%-10d %-20s%n", c.getId(), sdf.format(c.getDate()));
        }
    }
}